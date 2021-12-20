package io.github.mg138.bookshelf.item

import io.github.mg138.bookshelf.item.nbt.VanillaItemNbt
import io.github.mg138.bookshelf.item.util.Asset
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

object ServerItemManager {
    private val map: MutableMap<Identifier, ServerItem> = mutableMapOf()
    val ids
        get() = map.keys

    // register

    fun register(serverItem: ServerItem): ServerItem? {
        val config = serverItem.config
        val assets = config.assets

        Asset.copyTexture(assets.texture)
        Asset.copyModel(serverItem)

        return map.put(config.id, serverItem)
    }

    // get

    fun get(id: Identifier) = map[id]
    fun getAsItem(id: Identifier) = get(id)?.vanillaItem
    fun getAsItemStack(id: Identifier) = get(id)?.vanillaStack

    fun get(itemStack: ItemStack) = VanillaItemNbt.getItemId(itemStack)?.let { this.get(it) }
}