package io.github.mg138.bookshelf.utils

import net.minecraft.entity.Entity
import net.minecraft.particle.ParticleEffect
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

object ParticleUtil {
    fun Entity.spawnParticles(
        type: ParticleEffect,
        pos: Vec3d,
        count: Int,
        dPos: Vec3d,
        speed: Double
    ) {
        this.world.spawnParticles(type, pos, count, dPos, speed)
    }

    fun World.spawnParticles(
        type: ParticleEffect,
        pos: Vec3d,
        count: Int,
        dPos: Vec3d,
        speed: Double
    ) {
        (this as? ServerWorld)?.spawnParticles(type, pos.x, pos.y, pos.z, count, dPos.x, dPos.y, dPos.z, speed)
    }
}