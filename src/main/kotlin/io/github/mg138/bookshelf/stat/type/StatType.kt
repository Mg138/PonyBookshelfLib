package io.github.mg138.bookshelf.stat.type

import com.google.common.math.IntMath.pow
import io.github.mg138.bookshelf.stat.stat.Stat
import net.minecraft.text.LiteralText
import net.minecraft.text.MutableText
import net.minecraft.text.TextColor
import net.minecraft.text.TranslatableText
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier


abstract class StatType(
    val id: Identifier
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

    protected fun round(value: Stat, digitsAfterDecimal: Int): Stat {
        val m = pow(10, digitsAfterDecimal)

        return (value * m).round() / m
    }

    protected open fun valueToString(value: Stat, digitsAfterDecimal: Int) =
        round(value, digitsAfterDecimal).toString()

    protected open fun valueWithColor(value: Stat, digitsAfterDecimal: Int = 1): MutableText =
        LiteralText(valueToString(value, digitsAfterDecimal))
            .styled { it.withColor(numberColor) }

    val translationKey = "pony_bookshelf.stat_type.${id.namespace}.${id.path}"
    open fun name(stat: Stat = Stat.EMPTY) = TranslatableText(
        translationKey, valueWithColor(stat)
    )

    val indicatorTranslationKey = "$translationKey.indicator"
    open fun indicator(stat: Stat = Stat.EMPTY) = TranslatableText(
        indicatorTranslationKey, valueWithColor(stat)
    )
}