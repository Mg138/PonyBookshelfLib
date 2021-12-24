package io.github.mg138.bookshelf.item.test

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.item.BookItemSettings
import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.Preset
import io.github.mg138.bookshelf.stat.utils.StatMap
import io.github.mg138.bookshelf.utils.minus
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

class FabricItem : BookStatedItem(
    Main.modId - "fabric_item",
    BookItemSettings(true),
    FabricItemSettings(), Items.PAPER,
    StatMap().apply {
        putStat(Preset.DamageTypes.DAMAGE_AQUA, StatSingle(10000.0))
        putStat(Preset.ChanceTypes.CHANCE_CRITICAL, StatSingle(2.0))
        putStat(Preset.PowerTypes.POWER_CRITICAL, StatSingle(2.0))
    }
) {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        user.sendMessage(LiteralText("Hey!"), false)

        return super.use(world, user, hand)
    }
}