package io.github.mg138.bookshelf.stat.type

import io.github.mg138.bookshelf.result.UseOnEntityResult
import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.stat.stat.Stat
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.Style
import net.minecraft.text.TextColor
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World


abstract class StatType(
    val id: Identifier,
) {
    var numberColor: TextColor = TextColor.fromFormatting(Formatting.WHITE)!!

    constructor(id: Identifier, numberColor: TextColor?) : this(id) {
        numberColor?.let {
            this.numberColor = numberColor
        }
    }

    // Any

    override fun toString() = this.id.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StatType) return false

        return this.id == other.id
    }

    override fun hashCode() = this.id.hashCode()

    // Translate

    private fun damageWithColor(damage: Int) = LiteralText(damage.toString())
        .styled { it.withColor(numberColor) }

    val translationKey = "pony_bookshelf.stat_type.${id.namespace}.${id.path}"
    fun name(damage: Int) = TranslatableText(
        translationKey, damageWithColor(damage)
    )

    val indicatorTranslationKey = "$translationKey.indicator"
    fun indicator(damage: Int) = TranslatableText(
        indicatorTranslationKey, damageWithColor(damage)
    )

    // Event

    open fun onDamage(
        stat: Stat,
        statedItem: BookStatedItem,
        player: PlayerEntity,
        world: World,
        hand: Hand,
        entity: LivingEntity,
        hitResult: EntityHitResult?
    ): UseOnEntityResult? = null

    open val damagePriority: Int? = null
}