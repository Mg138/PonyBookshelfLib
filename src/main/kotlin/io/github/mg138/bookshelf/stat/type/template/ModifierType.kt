package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.utils.StatUtil
import net.minecraft.entity.LivingEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

abstract class ModifierType(id: Identifier) :
    PercentageStatType(id), StatEvent.OnDamageCallback {
    override val onDamagePriority = 1

    abstract class ModifierTypeTemplate(
        id: Identifier
    ) : ModifierType(id) {
        abstract fun condition(event: StatEvent.OnDamageCallback.OnDamageEvent): Boolean

        override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
            if (condition(event)) {
                val damagee = event.damagee

                if (damagee is LivingEntity) {
                    DamageManager[damagee].forEach { (type, other) ->
                        if (type is DamageType) {
                            DamageManager.replaceDamage(damagee, type, StatUtil.damageModifier(other, event.stat))
                        }
                    }
                }
            }
            return ActionResult.PASS
        }
    }

    abstract class StatTypeModifierTemplate(
        id: Identifier
    ) : ModifierType(id) {
        abstract fun condition(type: StatType): Boolean

        override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
            val damagee = event.damagee

            if (damagee is LivingEntity) {
                DamageManager[damagee].forEach { (type, other) ->
                    if (condition(type)) {
                        DamageManager.replaceDamage(damagee, type, other.modifier(event.stat.result()))
                    }
                }
            }
            return ActionResult.PASS
        }
    }
}