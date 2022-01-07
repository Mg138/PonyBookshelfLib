package io.github.mg138.bookshelf.item.type

import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

interface ProjectileThrower : Weapon {
    fun onRightClick(player: ServerPlayerEntity, itemStack: ItemStack): Boolean {
        return this.checkDelay(player, itemStack)
    }
}