package io.github.mg138.bookshelf.utils

import net.minecraft.entity.Entity
import net.minecraft.util.math.Vec3d

object EntityUtil {
    fun Entity.getDisplayPos(): Vec3d {
        val pos = this.pos
        return Vec3d(pos.x, this.eyeY, pos.z)
    }
}