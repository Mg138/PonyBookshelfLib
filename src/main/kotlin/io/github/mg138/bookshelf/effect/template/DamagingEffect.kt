package io.github.mg138.bookshelf.effect.template

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.effect.BookStatusEffectInfo
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.template.DamageType
import net.minecraft.entity.damage.DamageSource


abstract class DamagingEffect(
    private val delay: Int,
    private val damageType: DamageType
) : FlatEffect() {
    open fun visualEffect(effect: BookStatusEffectInfo) {
    }

    override fun act(effect: BookStatusEffectInfo, currentTime: Long) {
        val dT = currentTime - effect.startTime
        if (((dT + delay) % delay) != 0L) return

        val entity = effect.entity
        val power = effect.power
        DamageManager.queueDamage(entity, damageType, StatSingle(power), DamageSource.MAGIC)

        visualEffect(effect)
    }
}
