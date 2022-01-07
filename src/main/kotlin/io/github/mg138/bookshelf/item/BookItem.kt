package io.github.mg138.bookshelf.item

import eu.pb4.polymer.api.item.PolymerItem
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

interface BookItem : PolymerItem {
    val id: Identifier
    val bookItemSettings: BookItemSettings
    val settings: Item.Settings
    val vanillaItem: Item

    val customModelData: Int

    override fun getPolymerCustomModelData(itemStack: ItemStack, player: ServerPlayerEntity?) = customModelData
}