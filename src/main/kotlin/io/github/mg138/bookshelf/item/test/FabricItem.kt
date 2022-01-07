package io.github.mg138.bookshelf.item.test

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.item.BookItemSettings
import io.github.mg138.bookshelf.item.SimpleBookStaticStatedItem
import io.github.mg138.bookshelf.item.type.SimpleMeleeWeapon
import io.github.mg138.bookshelf.stat.data.StatMap
import io.github.mg138.bookshelf.stat.stat.StatRange
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.StatTypes
import io.github.mg138.bookshelf.utils.minus
import net.fabricmc.fabric.api.item.v1.FabricItemSettings
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.text.LiteralText
import net.minecraft.util.Hand
import net.minecraft.util.TypedActionResult
import net.minecraft.world.World

object FabricItem : SimpleBookStaticStatedItem(
    Main.modId - "fabric_item",
    BookItemSettings(true),
    FabricItemSettings(), Items.PAPER,
    StatMap().apply {
        putStat(StatTypes.DamageTypes.DAMAGE_AQUA, StatSingle(10000.0))
        putStat(StatTypes.DamageTypes.DAMAGE_LUMEN, StatRange(10000.0, 1000000.0))
        putStat(StatTypes.DamageTypes.DAMAGE_IGNIS, StatSingle(10000.0))
        putStat(StatTypes.DamageTypes.DAMAGE_TERRA, StatSingle(10000.0))
        putStat(StatTypes.DamageTypes.DAMAGE_TEMPUS, StatSingle(10000.0))
        putStat(StatTypes.ChanceTypes.CHANCE_CRITICAL, StatSingle(2.0))
        putStat(StatTypes.PowerTypes.POWER_CRITICAL, StatSingle(2.0))
    }
), SimpleMeleeWeapon {
    override fun use(world: World, user: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
        user.sendMessage(LiteralText("uwu"), false)

        return super.use(world, user, hand)
    }

    override val range = 8.0
}