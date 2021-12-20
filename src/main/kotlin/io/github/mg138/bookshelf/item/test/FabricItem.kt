package io.github.mg138.bookshelf.item.test

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.item.ServerItem
import io.github.mg138.bookshelf.item.ServerItemConfig
import io.github.mg138.bookshelf.item.util.Asset
import net.minecraft.entity.Entity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.TypedActionResult
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World

class FabricItem : ServerItem(
    ServerItemConfig(
        LiteralText("Fabric Item"),
        Asset(
            Asset.Model(
                "assets/pony_bookshelf/models/item/fabric_item.json", Main.modContainer.rootPath,
                Identifier(Main.modId, "fabric_item"), Items.PAPER, 1
            ),
            Asset.Texture("assets/pony_bookshelf/textures/item/fabric_item.png", Main.modContainer.rootPath)
        )
    )
) {
    override fun onAttackBlock(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        pos: BlockPos,
        direction: Direction
    ): ActionResult {
        Main.logger.info("${player.name.string} attacked block at ${pos.toShortString()}!")
        return super.onAttackBlock(player, world, hand, pos, direction)
    }

    override fun onAttackEntity(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        entity: Entity,
        hitResult: EntityHitResult?
    ): ActionResult {
        Main.logger.info("${player.name.string} attacked entity ${entity.name.string}!")
        return super.onAttackEntity(player, world, hand, entity, hitResult)
    }

    override fun onUseBlock(player: PlayerEntity, world: World, hand: Hand, hitResult: BlockHitResult?): ActionResult {
        Main.logger.info("${player.name.string} used on block at ${hitResult?.blockPos?.toShortString() ?: "(MISS)"}!")
        return super.onUseBlock(player, world, hand, hitResult)
    }

    override fun onUseEntity(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        entity: Entity,
        hitResult: EntityHitResult?
    ): ActionResult {
        Main.logger.info("${player.name.string} used on entity ${hitResult?.entity?.name?.string ?: "(MISS)"}!")
        return super.onUseEntity(player, world, hand, entity, hitResult)
    }

    override fun onUse(player: PlayerEntity, world: World, hand: Hand): TypedActionResult<ItemStack> {
        Main.logger.info("${player.name.string} used with ${hand.name}!")
        return super.onUse(player, world, hand)
    }
}