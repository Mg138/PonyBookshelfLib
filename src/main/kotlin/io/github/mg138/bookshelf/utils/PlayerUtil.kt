package io.github.mg138.bookshelf.utils

import io.github.mg138.bookshelf.item.type.Armor
import io.github.mg138.bookshelf.item.type.StatedItem
import io.github.mg138.player.data.ArmorManager
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand

object PlayerUtil {
    fun getArmor(player: ServerPlayerEntity): Map<ItemStack, StatedItem> {
        val items: MutableMap<ItemStack, StatedItem> = mutableMapOf()

        ArmorManager[player].asList().let { armorList ->
            armorList.forEach {
                val item = it.item

                if (item is StatedItem) {
                    items[it] = item
                }
            }
        }

        return items
    }

    fun getItemInHand(player: ServerPlayerEntity, hand: Hand): Pair<ItemStack, StatedItem>? {
        try {
            player.getStackInHand(hand).let { stackInHand ->
                val item = stackInHand.item
                if (item !is StatedItem || item is Armor) return null

                return stackInHand to item
            }
        } catch (_: Exception) {}

        return null
    }

    private fun MutableMap<ItemStack, StatedItem>.hand(player: ServerPlayerEntity, hand: Hand) {
        getItemInHand(player, hand)?.let { (itemStack, item) ->
            this[itemStack] = item
        }
    }

    fun getItems(player: ServerPlayerEntity): Map<ItemStack, StatedItem> {
        val map: MutableMap<ItemStack, StatedItem> = mutableMapOf()

        map.putAll(getArmor(player))
        //map.hand(player, Hand.MAIN_HAND)
        //not needed, very dangerous
        map.hand(player, Hand.OFF_HAND)

        return map
    }
}