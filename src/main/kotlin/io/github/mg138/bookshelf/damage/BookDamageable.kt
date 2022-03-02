package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.entity.StatedEntity
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.StatTypes
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource

interface BookDamageable {
    fun bookDamage(amount: Double, source: DamageSource) {
        defaultBookDamage(amount, source, this)
    }

    companion object {
        fun defaultBookDamage(amount: Double, source: DamageSource, damageable: BookDamageable) {
            if (damageable !is LivingEntity) return

            if (damageable is StatedEntity) {
                damageable.getStats().addStat(StatTypes.MiscTypes.Health, StatSingle(-amount))
            } else {
                damageable.damage(source, amount.toFloat())
            }
        }
    }
}