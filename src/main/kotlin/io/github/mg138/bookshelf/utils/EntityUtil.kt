package io.github.mg138.bookshelf.utils

import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.math.Vec3d

object EntityUtil {
    fun Entity.getDisplayPos(): Vec3d {
        val pos = this.pos
        return Vec3d(pos.x, this.eyeY, pos.z)
    }

    fun Entity.canHit(): Boolean {
        val creativeFlag = (this as? PlayerEntity)?.let {
            !this.isCreative
        } ?: true

        return this !is PlayerEntity && !this.isSpectator && this.isAlive && this.collides() && !this.isInvulnerable && !this.isInvisible && creativeFlag
    }
}