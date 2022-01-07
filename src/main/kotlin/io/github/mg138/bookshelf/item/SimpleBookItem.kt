package io.github.mg138.bookshelf.item

import eu.pb4.polymer.api.item.SimplePolymerItem
import eu.pb4.polymer.api.resourcepack.PolymerRPUtils
import io.github.mg138.bookshelf.utils.minus
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.TranslatableText
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

abstract class SimpleBookItem(
    final override val id: Identifier,
    final override val bookItemSettings: BookItemSettings,
    final override val settings: Settings,
    final override val vanillaItem: Item
) : SimplePolymerItem(settings, vanillaItem), BookItem {
    override val customModelData = if (bookItemSettings.customTexture) {
        PolymerRPUtils.requestModel(vanillaItem, id.namespace - "item/${id.path}").value
    } else -1

    open val loreKey1 = "item.${id.namespace}.${id.path}.lore1"
    open val loreKey2 = "item.${id.namespace}.${id.path}.lore2"
    open val loreKey3 = "item.${id.namespace}.${id.path}.lore3"

    override fun appendTooltip(
        stack: ItemStack,
        world: World?,
        tooltip: MutableList<Text>,
        context: TooltipContext
    ) {
        tooltip.add(TranslatableText(loreKey1))
        tooltip.add(TranslatableText(loreKey2))
        tooltip.add(TranslatableText(loreKey3))
    }

    override fun getPolymerCustomModelData(itemStack: ItemStack, player: ServerPlayerEntity?) = customModelData

    override fun damage(source: DamageSource) = false

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return TypedActionResult.fail(user.getStackInHand(hand))
    }

    open fun register() {
        Registry.register(Registry.ITEM, id, this)
    }
}