package io.github.mg138.bookshelf.item

import io.github.mg138.bookshelf.stat.StatMap
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

abstract class BookStaticStatedItem(
    id: Identifier,
    bookItemSettings: BookItemSettings,
    settings: Settings, vanillaItem: Item,
    private val statMap: StatMap
) : BookStatedItem(id, bookItemSettings, settings, vanillaItem) {
    override fun getStatMap(itemStack: ItemStack?) = statMap
}