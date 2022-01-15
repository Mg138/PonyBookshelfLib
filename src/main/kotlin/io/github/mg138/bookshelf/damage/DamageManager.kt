package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.entity.BookStatedEntity
import io.github.mg138.bookshelf.item.type.Armor
import io.github.mg138.bookshelf.item.type.ProjectileThrower
import io.github.mg138.bookshelf.item.type.StatedItem
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.player.data.ArmorManager
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
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World

object DamageManager {
    private val map: MutableMap<LivingEntity, MutableMap<StatType, Stat>> = mutableMapOf()

    operator fun get(damagee: LivingEntity) = map.getOrPut(damagee) { mutableMapOf() }

    fun queueDamage(damagee: LivingEntity?, type: StatType, damage: Stat) {
        if (damagee == null) return

        val damages = get(damagee)

        damages[type]?.let {
            damages[type] = it + damage
        } ?: run {
            damages[type] = damage
        }
    }

    fun replaceDamage(damagee: LivingEntity, type: StatType, damage: Stat) {
        val damages = get(damagee)

        damages[type] = damage
    }

    fun afterDamage(items: Map<ItemStack, StatedItem>, damager: LivingEntity, damagee: LivingEntity? = null) {
        items.forEach { (itemStack, item) ->
            item.afterAttackEntity(itemStack, damager, damagee)

            if (damagee is BookStatedEntity<*>) {
                damagee.afterBeingAttacked(damager)
            }
        }
    }

    fun resolveDamage(
        damagee: LivingEntity? = null,
        damager: LivingEntity? = null,
        items: Map<ItemStack, StatedItem> = mapOf(),
        source: DamageSource = DamageSource.GENERIC
    ) {
        val damages: MutableMap<StatType, Double> = mutableMapOf()

        this.map[damagee]?.onEach { (type, stat) ->
            val damage = stat.result()

            damagee?.damage(source, damage.toFloat())

            damages[type] = damage

            if (damagee != null) {
                DamageIndicatorManager.displayDamage(damage, type, damagee)
            }

            if (damager != null) {
                afterDamage(items, damager, damagee)
            }
        }?.clear()

        if (damagee != null) {
            DamageEvent.AFTER_BOOK_DAMAGE.invoker().afterDamage(
                DamageEvent.AfterBookDamageCallback.AfterBookDamageEvent(damager, damagee, items, damages)
            )
        }

        this.map.remove(damagee)
    }

    fun onPlayerAttackLivingEntity(
        damager: ServerPlayerEntity,
        damagee: LivingEntity?,
        items: Map<ItemStack, StatedItem>
    ): ActionResult {
        for ((itemStack, item) in items) {
            val result = item.onAttackEntity(itemStack, damager, damagee)

            if (result != ActionResult.PASS) return result
        }

        if (damagee is BookStatedEntity<*>) {
            damagee.onBeingAttacked(damager)
        }

        DamageEvent.ON_BOOK_DAMAGE.invoker().onDamage(
            DamageEvent.OnBookDamageCallback.OnBookDamageEvent(damager, damagee, items)
        )

        resolveDamage(damagee, damager, items, DamageSource.player(damager))

        return ActionResult.PASS
    }

    fun onPlayerAttack(
        damager: ServerPlayerEntity,
        damagee: Entity,
        items: Map<ItemStack, StatedItem>
    ): ActionResult {
        if (damagee !is LivingEntity) return ActionResult.PASS
        if (damagee is DamageIndicatorManager.Indicator) return ActionResult.FAIL
        if (damagee.isDead) return ActionResult.FAIL

        return onPlayerAttackLivingEntity(damager, damagee, items)
    }

    fun getArmor(player: ServerPlayerEntity): Map<ItemStack, StatedItem> {
        val items: MutableMap<ItemStack, StatedItem> = mutableMapOf()

        ArmorManager[player].asList().let { armorList ->
            armorList.forEach {
                val item = it.item

                if (item is StatedItem) {
                    items[it] = item
                }
            }
        }

        return items
    }

    fun getItemInHand(player: ServerPlayerEntity, hand: Hand): Pair<ItemStack, StatedItem>? {
        player.getStackInHand(hand).let { stackInHand ->
            val item = stackInHand.item
            if (item !is StatedItem || item is Armor) return null

            return stackInHand to item
        }
    }

    private fun onPlayerAttack(
        damager: PlayerEntity,
        world: World,
        hand: Hand,
        damagee: Entity,
        hitResult: EntityHitResult?
    ): ActionResult {
        if (damager !is ServerPlayerEntity) return ActionResult.PASS

        val items: MutableMap<ItemStack, StatedItem> = mutableMapOf()

        getItemInHand(damager, hand)?.let { (itemStack, item) ->
            items.put(itemStack, item)
        } ?: return ActionResult.FAIL

        items.putAll(getArmor(damager))

        return onPlayerAttack(damager, damagee, items)
    }

    fun onPlayerUseItem(player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        val itemStack = player.getStackInHand(hand)

        if (player is ServerPlayerEntity) {
            val item = itemStack.item

            if (item is ProjectileThrower) {
                item.onRightClick(player, itemStack)
            }
        }
        return TypedActionResult.pass(itemStack)
    }

    fun register() {
        AttackEntityCallback.EVENT.register(this::onPlayerAttack)
        UseEntityCallback.EVENT.register { player, _, hand, _, _ ->
            onPlayerUseItem(player, hand).result
        }
        UseItemCallback.EVENT.register { player, _, hand ->
            onPlayerUseItem(player, hand)
        }
    }
}