package io.github.mg138.bookshelf.effect.impl

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.effect.BookStatusEffectInfo
import io.github.mg138.bookshelf.effect.StatusEffectManager
import io.github.mg138.bookshelf.effect.template.DamagingEffect
import io.github.mg138.bookshelf.stat.type.StatTypes
import io.github.mg138.bookshelf.utils.ParticleUtil.spawnParticles
import io.github.mg138.bookshelf.utils.minus
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.Vec3d

object Burning : DamagingEffect(
    10,
    StatTypes.DamageTypes.DamageIgnis
) {
    override fun visualEffect(effect: BookStatusEffectInfo) {
        val entity = effect.entity
        val power = effect.power.toInt()

        val pos = entity.pos.add(0.0, 1.0, 0.0)
        val dPos = Vec3d.ZERO
        val count = power / 16 + 10

        entity.spawnParticles(
            ParticleTypes.LAVA,
            pos, count, dPos, 0.5
        )
    }


    fun register() {
        StatusEffectManager.register(Main.modId - "burning", this)
    }
}