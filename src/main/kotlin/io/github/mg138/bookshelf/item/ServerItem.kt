package io.github.mg138.bookshelf.item

import io.github.mg138.bookshelf.item.nbt.VanillaItemNbt
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

open class ServerItem(val config: ServerItemConfig) {
    val vanillaItem
        get() = config.vanillaItem

    val vanillaStack: ItemStack = vanillaItem.defaultStack.apply {
        setCustomName(config.name)
    }.also {
        VanillaItemNbt.putItemData(it, this)
    }

    open fun onAttackBlock(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        pos: BlockPos,
        direction: Direction
    ) = ActionResult.PASS

    open fun onAttackEntity(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        entity: Entity,
        hitResult: EntityHitResult?
    ) = ActionResult.PASS

    open fun onUseBlock(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        hitResult: BlockHitResult?
    ) = ActionResult.PASS

    open fun onUseEntity(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        entity: Entity,
        hitResult: EntityHitResult?
    ) = ActionResult.PASS

    open fun onUse(
        player: PlayerEntity,
        world: World,
        hand: Hand
    ) = TypedActionResult.pass(vanillaStack)
}