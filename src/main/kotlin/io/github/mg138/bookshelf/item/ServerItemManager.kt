package io.github.mg138.bookshelf.item

import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object ServerItemManager {
    private val map: MutableMap<Identifier, BookItem> = mutableMapOf()
    val ids
        get() = map.keys

    // register

    fun register(serverItem: BookItem): BookItem? {
        val id = serverItem.id

        Registry.register(Registry.ITEM, id, serverItem)

        return map.put(id, serverItem)
    }

    // get

    fun get(id: Identifier) = map[id]
}