package io.github.mg138.bookshelf.item

import eu.pb4.polymer.api.item.SimplePolymerItem
import eu.pb4.polymer.api.resourcepack.PolymerRPUtils
import io.github.mg138.bookshelf.utils.minus
import net.minecraft.entity.damage.DamageSource
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

abstract class BookItem(
    val id: Identifier,
    bookItemSettings: BookItemSettings,
    settings: Settings, vanillaItem: Item
) : SimplePolymerItem(settings, vanillaItem) {
    val customModelData = if (bookItemSettings.customTexture) {
        PolymerRPUtils.requestModel(vanillaItem, id.namespace - "item/${id.path}").value
    } else -1

    override fun getPolymerCustomModelData(itemStack: ItemStack, player: ServerPlayerEntity?) = customModelData

    override fun damage(source: DamageSource) = false
}