package io.github.mg138.bookshelf.item.type

import io.github.mg138.bookshelf.projectile.WandProjectile
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

interface SimpleWand : ProjectileThrower {
    val speed: Double

    fun spawnProjectile(player: ServerPlayerEntity, itemStack: ItemStack) {
        val world = player.world
        val entity = WandProjectile(player, world).also { projectile ->
            projectile.itemStack = itemStack
            projectile.velocity = player.getRotationVec(1.0F).multiply(speed)
        }
        world.spawnEntity(entity)
    }

    override fun onRightClick(player: ServerPlayerEntity, itemStack: ItemStack): Boolean {
        if (!super.onRightClick(player, itemStack)) return false

        spawnProjectile(player, itemStack)

        return true
    }
}