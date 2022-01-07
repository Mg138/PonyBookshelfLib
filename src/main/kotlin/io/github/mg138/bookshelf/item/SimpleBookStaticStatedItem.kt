package io.github.mg138.bookshelf.item

import io.github.mg138.bookshelf.stat.data.StatMap
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.Identifier

abstract class SimpleBookStaticStatedItem(
    id: Identifier,
    bookItemSettings: BookItemSettings,
    settings: Settings, vanillaItem: Item,
    private val statMap: StatMap
) : SimpleBookStatedItem(id, bookItemSettings, settings, vanillaItem) {
    override fun getStatMap(itemStack: ItemStack?) = statMap
}