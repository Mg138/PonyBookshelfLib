package io.github.mg138.bookshelf.effect

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.effect.template.DamagingEffect
import io.github.mg138.bookshelf.stat.type.Preset
import io.github.mg138.bookshelf.utils.EntityUtil.getDisplayPos
import io.github.mg138.bookshelf.utils.ParticleUtil.spawnParticles
import io.github.mg138.bookshelf.utils.minus
import net.minecraft.entity.LivingEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry

class Burning : DamagingEffect(
    Preset.DamageTypes.DAMAGE_IGNIS_COLOR,
    10,
    Preset.DamageTypes.DAMAGE_IGNIS
) {
    override fun visualEffect(entity: LivingEntity, amplifier: Int) {
        val pos = entity.pos
        val dPos = Vec3d(0.0, 0.0, 0.0)
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