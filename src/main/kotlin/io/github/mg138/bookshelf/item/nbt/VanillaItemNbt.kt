package io.github.mg138.bookshelf.item.nbt

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.item.ServerItem
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object VanillaItemNbt {
    const val itemIdKey = "${Main.modId}:item_id"

    fun putItemData(itemStack: ItemStack, serverItem: ServerItem) {
        itemStack.orCreateNbt.putString(itemIdKey, serverItem.config.id.toString())
        itemStack.orCreateNbt.putInt("CustomModelData", serverItem.config.assets.model.customModelData)
    }

    fun getItemId(itemStack: ItemStack): Identifier? {
        if (!itemStack.hasNbt()) return null

        itemStack.nbt?.let { nbt ->
            return Identifier(nbt.getString(itemIdKey))
        }
        return null
    }
}