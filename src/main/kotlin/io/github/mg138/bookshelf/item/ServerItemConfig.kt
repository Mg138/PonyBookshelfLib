package io.github.mg138.bookshelf.item

import io.github.mg138.bookshelf.item.util.Asset
import net.minecraft.item.Item
import net.minecraft.text.Text
import net.minecraft.util.Identifier

open class ServerItemConfig(
    val name: Text,
    val assets: Asset
) {
    val vanillaItem: Item
        get() = assets.model.vanillaItem

    val id: Identifier
        get() = assets.model.id
}