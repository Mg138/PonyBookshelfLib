package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.entity.StatedEntity
import io.github.mg138.bookshelf.item.type.ProjectileThrower
import io.github.mg138.bookshelf.stat.data.MutableStats
import io.github.mg138.bookshelf.stat.data.StatMap
import io.github.mg138.bookshelf.stat.data.Stats
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.type.StatTypes
import io.github.mg138.bookshelf.utils.StatUtil
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult

object DamageManager {
    class DamageInfos {
        val map: MutableMap<DamageSource, MutableStats> = mutableMapOf()

        operator fun get(source: DamageSource) = map.getOrPut(source) { StatMap() }
    }

    private val map: MutableMap<LivingEntity, DamageInfos> = mutableMapOf()

    operator fun get(damagee: LivingEntity) = map.getOrPut(damagee) { DamageInfos() }

    fun queueDamage(damagee: LivingEntity?, type: StatType, damage: Stat, source: DamageSource?) {
        if (damagee == null) return

        val damages = this[damagee][source ?: DamageSource.GENERIC]

        damages[type] = damage + damages[type]
    }

    fun replaceDamage(damagee: LivingEntity, type: StatType, damage: Stat, predicate: (DamageSource) -> Boolean) {
        this[damagee].map.forEach { (source, stats) ->
            if (predicate(source)) {
                stats[type] = damage
            }
        }
    }

    fun replaceDamage(
        damagee: LivingEntity,
        type: StatType,
        damage: Stat,
        source: DamageSource = DamageSource.GENERIC
    ) {
        replaceDamage(damagee, type, damage) { it == source }
    }

    fun replaceDamageOfAllSource(
        damagee: LivingEntity,
        type: StatType,
        damage: Stat
    ) {
        replaceDamage(damagee, type, damage) { true }
    }

    private fun afterDamage(
        damagee: LivingEntity,
        damageeStats: Stats?,
        damages: Map<StatType, Double>,
        damager: Entity?,
        source: DamageSource?
    ) {
        DamageEvent.AFTER_BOOK_DAMAGE.invoker().afterDamage(
            DamageEvent.AfterBookDamageCallback.AfterBookDamageEvent(damagee, damages, damager, source)
        )
        if (damager is StatedEntity) {
            StatUtil.afterDamage(damagee, damageeStats, damager, damager.getStats(), source)
        }
    }

    fun vanillaDamage(
        damagee: LivingEntity,
        damageeStats: Stats? = null,
        damage: Float,
        source: DamageSource
    ): ActionResult {
        val stats = StatMap()
        val damageDouble = damage.toDouble()

        if (source.isFire) {
            stats.addStat(StatTypes.DamageTypes.DamageIgnis, StatSingle(damageDouble))
        } else if (source == DamageSource.DROWN) {
            stats.addStat(StatTypes.DamageTypes.DamageAqua, StatSingle(damageDouble))
        } else {
            stats.addStat(StatTypes.DamageTypes.DamageNone, StatSingle(damageDouble))
        }

        return StatUtil.onDamage(damagee, damageeStats, null, stats, source)
    }

    fun damage(
        damagee: LivingEntity,
        damageeStats: Stats? = null,
        damager: Entity?,
        damagerStats: Stats? = null,
        source: DamageSource
    ): ActionResult {
        /*
        DamageEvent.ON_BOOK_DAMAGE.invoker().onDamage(
            DamageEvent.OnBookDamageCallback.OnBookDamageEvent(damager, damagee, )
        )
        */

        return StatUtil.onDamage(damagee, damageeStats, damager, damagerStats, source)
    }


    fun attack(
        damager: LivingEntity,
        damagerExtraStats: Stats? = null,
        damagee: LivingEntity,
        damageeExtraStats: Stats? = null,
    ): ActionResult {
        if (damager is StatedEntity && damagee is StatedEntity) {
            val damagerStats = damager.getStats().copy().addAll(damagerExtraStats)
            val damageeStats = damagee.getStats().copy().addAll(damageeExtraStats)

            return damage(damagee, damageeStats, damager, damagerStats, DamageSource.mob(damager))
        }

        if (damager is StatedEntity) {
            val damagerStats = damager.getStats().copy().addAll(damagerExtraStats)

            return damage(damagee, null, damager, damagerStats, DamageSource.mob(damager))
        }

        return damage(damagee, null, damager, null, DamageSource.mob(damager))
    }

    private fun resolveDamage() {
        this.map.forEach { (damagee, damageInfo) ->
            damageInfo.map.forEach { (source, stats) ->
                val damages: MutableMap<StatType, Double> = mutableMapOf()

                stats.forEach { (type, stat) ->
                    val damage = stat.result()

                    if (damagee is BookDamageable) {
                        damagee.bookDamage(damage, source)
                    } else {
                        damagee.damage(source, damage.toFloat())
                    }

                    damages[type] = damage

                    DamageIndicatorManager.displayDamage(damage, type, damagee)
                }

                afterDamage(damagee, stats, damages, source.source, source)
            }
        }

        this.map.clear()
    }

    private fun onPlayerUseItem(player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = player.getStackInHand(hand)

        if (player !is ServerPlayerEntity) return TypedActionResult.pass(itemStack)

        val item = itemStack.item

        if (item is ProjectileThrower) {
            item.onRightClick(player, itemStack)
        }

        return TypedActionResult.pass(itemStack)
    }

    fun register() {
        AttackEntityCallback.EVENT.register { _, _, _, _, _ ->
            ActionResult.FAIL
        }

        UseEntityCallback.EVENT.register { player, _, hand, _, _ ->
            onPlayerUseItem(player, hand).result
        }

        UseItemCallback.EVENT.register { player, _, hand ->
            onPlayerUseItem(player, hand)
        }

        ServerTickEvents.END_WORLD_TICK.register {
            resolveDamage()
        }
    }
}