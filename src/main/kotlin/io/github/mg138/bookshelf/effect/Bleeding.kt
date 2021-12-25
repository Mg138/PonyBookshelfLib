package io.github.mg138.bookshelf.effect

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.effect.template.DamagingEffect
import io.github.mg138.bookshelf.stat.type.Preset
import io.github.mg138.bookshelf.utils.EntityUtil.getDisplayPos
import io.github.mg138.bookshelf.utils.ParticleUtil.spawnParticles
import io.github.mg138.bookshelf.utils.minus
import net.minecraft.block.Blocks
import net.minecraft.entity.LivingEntity
import net.minecraft.particle.BlockStateParticleEffect
import net.minecraft.particle.ParticleTypes
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry

class Bleeding : DamagingEffect(
    Preset.DamageTypes.DAMAGE_BLEED_COLOR,
    7,
    Preset.DamageTypes.DAMAGE_BLEED
) {
    override fun visualEffect(entity: LivingEntity, amplifier: Int) {
        val pos = entity.getDisplayPos()
        val dPos = Vec3d(0.0, 0.0, 0.0)
        val count = amplifier / 4 + 10

        entity.spawnParticles(
            BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultState),
            pos, count, dPos, 0.5
        )
    }

    companion object {
        lateinit var BLEEDING: Bleeding

        fun register() {
            BLEEDING = Registry.register(
                Registry.STATUS_EFFECT,
                Main.modId - "bleeding", Bleeding()
            )
        }
    }
}