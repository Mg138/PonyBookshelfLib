package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.result.DamageResult
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.EntityDamageSource
import net.minecraft.util.ActionResult

object DamageManager {
    private val map: MutableMap<LivingEntity, MutableMap<StatType, Stat>> = mutableMapOf()

    fun get(damagee: LivingEntity) = map[damagee]

    fun queueDamage(damagee: LivingEntity, damage: DamageResult) {
        val damages = map.getOrPut(damagee) { mutableMapOf() }
        val type = damage.type

        damages[type]?.let {
            damages[type] = it + damage.damage
        } ?: run {
            damages[type] = damage.damage
        }
    }

    fun resolveDamage(damagee: LivingEntity, damager: Entity) {
        map[damagee]?.also { damages ->
            // TODO entity defense
            val source = EntityDamageSource("entity", damager)

            damages.forEach { (type, stat) ->
                val damage = stat.result()

                damagee.damage(source, damage.toFloat())
                DamageIndicatorManager.displayDamage(damage, type, damagee)
            }
        }?.clear()

        map.remove(damagee)
    }

    fun register() {
        AttackEntityCallback.EVENT.register attackEntity@{ player, world, hand, entity, hitResult ->
            if (entity is DamageIndicatorManager.Indicator) return@attackEntity ActionResult.FAIL

            (player.inventory.mainHandStack.item as? BookStatedItem)?.onPlayerAttackEntity(
                player,
                world,
                hand,
                entity,
                hitResult
            ) ?: ActionResult.PASS
        }
    }
}