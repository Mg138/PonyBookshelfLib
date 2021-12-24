package io.github.mg138.bookshelf.effect.template

import eu.pb4.polymer.api.other.PolymerStatusEffect
import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.template.DamageType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.entity.effect.StatusEffectCategory


abstract class DamagingEffect(
    color: Int,
    private val delay: Int,
    private val damageType: DamageType
) : StatusEffect(StatusEffectCategory.HARMFUL, color), PolymerStatusEffect {
    open fun visualEffect(entity: LivingEntity, amplifier: Int) {
    }

    override fun canApplyUpdateEffect(duration: Int, amplifier: Int): Boolean {
        return (duration % delay) == 0
    }

    override fun applyUpdateEffect(entity: LivingEntity, amplifier: Int) {
        DamageManager.queueDamage(entity, damageType, StatSingle(amplifier.toDouble()))
        DamageManager.resolveDamage(entity)
        visualEffect(entity, amplifier)
    }
}
