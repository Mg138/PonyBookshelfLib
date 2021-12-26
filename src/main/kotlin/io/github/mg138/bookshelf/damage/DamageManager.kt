package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.entity.BookStatedEntity
import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World

object DamageManager {
    private val map: MutableMap<LivingEntity, MutableMap<StatType, Stat>> = mutableMapOf()

    operator fun get(damagee: LivingEntity) = map[damagee]

    fun queueDamage(damagee: LivingEntity, type: StatType, damage: Stat) {
        val damages = map.getOrPut(damagee) { mutableMapOf() }

        damages[type]?.let {
            damages[type] = it + damage
        } ?: run {
            damages[type] = damage
        }
    }

    fun resolveDamage(
        damagee: LivingEntity,
        itemStack: ItemStack? = null,
        item: BookStatedItem? = null,
        damager: LivingEntity? = null,
        source: DamageSource = DamageSource.GENERIC
    ) {
        val damages: MutableMap<StatType, Double> = mutableMapOf()

        map[damagee]?.onEach { (type, stat) ->
            val damage = stat.result()
            damagee.damage(source, damage.toFloat())
            damages[type] = damage

            if (damager != null) {
                DamageIndicatorManager.displayDamage(damage, type, damagee)

                if (item != null) {
                    item.afterAttackEntity(itemStack, damager, damagee)

                    if (damagee is BookStatedEntity<*>) {
                        damagee.afterBeingAttacked(damager, item)
                    }
                }
            }
        }?.clear()

        DamageEvent.AFTER_BOOK_DAMAGE.invoker().afterDamage(
            DamageEvent.AfterBookDamageCallback.AfterBookDamageEvent(damages, item, damager, damagee)
        )

        map.remove(damagee)
    }

    private fun onPlayerAttackEntity(
        damager: PlayerEntity,
        world: World,
        hand: Hand,
        damagee: Entity,
        hitResult: EntityHitResult?
    ): ActionResult {
        if (damagee !is LivingEntity) return ActionResult.PASS
        if (damagee.isDead) return ActionResult.FAIL
        if (damagee is DamageIndicatorManager.Indicator) return ActionResult.FAIL

        val itemStack = damager.getStackInHand(hand)
        val item = itemStack.item as? BookStatedItem ?: return ActionResult.PASS

        val result = item.onAttackEntity(itemStack, damager, hand, damagee, hitResult, world)
        if (result != ActionResult.PASS) return result

        if (damagee is BookStatedEntity<*>) {
            damagee.onBeingAttacked(item, damager, world, hand, hitResult)
        }

        DamageEvent.ON_BOOK_DAMAGE.invoker().onDamage(
            DamageEvent.OnBookDamageCallback.OnBookDamageEvent(item, damager, damagee, world, hand, hitResult)
        )
        resolveDamage(damagee, itemStack, item, damager, DamageSource.player(damager))

        return result
    }

    fun register() {
        AttackEntityCallback.EVENT.register(this::onPlayerAttackEntity)
    }
}