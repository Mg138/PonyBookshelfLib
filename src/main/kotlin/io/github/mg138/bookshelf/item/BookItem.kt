package io.github.mg138.bookshelf.item

import eu.pb4.polymer.api.item.SimplePolymerItem
import eu.pb4.polymer.api.resourcepack.PolymerRPUtils
import io.github.mg138.bookshelf.utils.minus
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

abstract class BookItem(
    val id: Identifier,
    bookItemSettings: BookItemSettings,
    settings: Settings, vanillaItem: Item
) : SimplePolymerItem(settings, vanillaItem) {
    private val customModelData = if (bookItemSettings.customTexture) {
        PolymerRPUtils.requestModel(vanillaItem, id.namespace - "item/${id.path}").value
    } else -1

    override fun getPolymerCustomModelData(itemStack: ItemStack, player: ServerPlayerEntity?) = customModelData

    override fun damage(source: DamageSource) = false

    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        return TypedActionResult.fail(user.getStackInHand(hand))
    }

    open fun register() {
        Registry.register(Registry.ITEM, id, this)
    }
}