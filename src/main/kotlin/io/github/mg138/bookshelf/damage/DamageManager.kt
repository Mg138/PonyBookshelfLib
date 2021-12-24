package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.EntityDamageSource
import net.minecraft.util.ActionResult

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

    fun resolveDamage(damagee: LivingEntity, damager: Entity) {
        map[damagee]?.also { damages ->
            // TODO entity defense
            val source = EntityDamageSource(damager.entityName, damager)

            damages.forEach { (type, stat) ->
                val damage = stat.result()

                damagee.damage(source, damage.toFloat())
                DamageIndicatorManager.displayDamage(damage, type, damagee)

                if (damager is LivingEntity) {
                    (damager.mainHandStack.item as? BookStatedItem)?.let {
                        it.afterAttackEntity(damager, damagee)
                    }
                }
            }
        }?.clear()

        map.remove(damagee)
    }

    fun register() {
        AttackEntityCallback.EVENT.register attackEntity@{ player, world, hand, entity, hitResult ->
            if (entity !is LivingEntity) return@attackEntity ActionResult.PASS
            if (entity is DamageIndicatorManager.Indicator) return@attackEntity ActionResult.FAIL

            (player.mainHandStack.item as? BookStatedItem)?.onAttackEntity(
                player,
                world,
                hand,
                entity,
                hitResult
            ) ?: ActionResult.PASS
        }
    }
}