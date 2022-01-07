package io.github.mg138.bookshelf.effect

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.effect.template.DamagingEffect
import io.github.mg138.bookshelf.stat.type.StatTypes
import io.github.mg138.bookshelf.utils.ParticleUtil.spawnParticles
import io.github.mg138.bookshelf.utils.minus
import net.minecraft.entity.LivingEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry

class Burning : DamagingEffect(
    StatTypes.DamageTypes.DAMAGE_IGNIS_COLOR,
    10,
    StatTypes.DamageTypes.DAMAGE_IGNIS
) {
    override fun visualEffect(entity: LivingEntity, amplifier: Int) {
        val pos = entity.pos.add(0.0, 1.0, 0.0)
        val dPos = Vec3d.ZERO
        val count = amplifier / 16 + 10

        entity.spawnParticles(
            ParticleTypes.LAVA,
            pos, count, dPos, 0.5
        )
    }

    companion object {
        lateinit var BURNING: Burning

        fun register() {
            BURNING = Registry.register(
                Registry.STATUS_EFFECT,
                Main.modId - "burning", Burning()
            )
        }
    }
}