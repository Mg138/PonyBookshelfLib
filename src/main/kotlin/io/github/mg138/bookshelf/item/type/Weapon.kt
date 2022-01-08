package io.github.mg138.bookshelf.item.type

import eu.pb4.polymer.api.item.PolymerItem
import io.github.mg138.bookshelf.stat.type.StatTypes
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.CooldownUpdateS2CPacket
import net.minecraft.server.network.ServerPlayerEntity

interface Weapon {
    fun checkDelay(player: ServerPlayerEntity, itemStack: ItemStack): Boolean {
        if (this is StatedItem) {
            val canDamage = StatTypes.MiscTypes.ATTACK_DELAY.canDamage(player)

            if (canDamage) {
                this.getStat(StatTypes.MiscTypes.ATTACK_DELAY, itemStack)?.let {
                    val delay = it.result().toInt()
                    StatTypes.MiscTypes.ATTACK_DELAY.setDelay(player, delay)

                    if (this is PolymerItem) {
                        player.networkHandler.sendPacket(
                            CooldownUpdateS2CPacket(this.getPolymerItem(itemStack, player), delay)
                        )
                    }
                }
            }

            return canDamage
        }
        return false
    }
}