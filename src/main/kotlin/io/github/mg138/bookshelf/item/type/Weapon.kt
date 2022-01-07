package io.github.mg138.bookshelf.item.type

import io.github.mg138.bookshelf.stat.type.StatTypes
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

interface Weapon {
    fun checkDelay(player: ServerPlayerEntity, itemStack: ItemStack): Boolean {
        if (this is StatedItem) {
            val result = StatTypes.MiscTypes.ATTACK_DELAY.canDamage(player)

            this.getStat(StatTypes.MiscTypes.ATTACK_DELAY, itemStack)?.let {
                StatTypes.MiscTypes.ATTACK_DELAY.setDelay(player, it.result().toInt())
            }

            return result
        }
        return false
    }
}