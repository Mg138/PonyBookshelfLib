package io.github.mg138.bookshelf.item

import net.minecraft.client.item.TooltipContext
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import net.minecraft.world.World

abstract class SimpleBookStatedItem(
    id: Identifier,
    bookItemSettings: BookItemSettings,
    settings: Settings, vanillaItem: Item
) : SimpleBookItem(id, bookItemSettings, settings, vanillaItem), BookStatedItem {
    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        tooltip.addAll(getStatMap(stack).lores())
        super.appendTooltip(stack, world, tooltip, context)
    }
}