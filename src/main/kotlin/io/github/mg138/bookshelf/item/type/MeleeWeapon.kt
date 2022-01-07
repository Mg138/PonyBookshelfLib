package io.github.mg138.bookshelf.item.type

import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

interface MeleeWeapon : Weapon {
    fun onLeftClick(player: ServerPlayerEntity, itemStack: ItemStack): Boolean {
        return this.checkDelay(player, itemStack)
    }
}