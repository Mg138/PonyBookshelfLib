package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.stat.StatRange
import io.github.mg138.bookshelf.stat.type.LoredStatType
import net.minecraft.text.Text
import net.minecraft.text.TextColor
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

abstract class PercentageStatType(id: Identifier) : LoredStatType(id) {
    override fun valueToString(value: Stat, digitsAfterDecimal: Int) =
        round(value, digitsAfterDecimal).toString() + "%"

    companion object {
        private const val GREEN = 0x69ff2f
        private const val RED = 0xd2625e
        private const val ORANGE = 0xffb34b
    }

    private fun getColor(stat: Stat): TextColor {
        if (stat is StatRange) {
            return if (stat.min < 0 && stat.max > 0) {
                TextColor.fromRgb(ORANGE)
            } else if (stat.min > 0) {
                TextColor.fromRgb(GREEN)
            } else {
                TextColor.fromRgb(RED)
            }
        }

        val result = stat.result()

        return if (result > 0) {
            TextColor.fromRgb(GREEN)
        } else {
            TextColor.fromRgb(RED)
        }
    }

    override fun nameText(stat: Stat): Text = TranslatableText(
        translationKey, valueToString(stat * 100.0, 2)
    ).styled { it.withColor(getColor(stat)) }

    override fun indicatorText(stat: Stat): Text = TranslatableText(
        indicatorTranslationKey, valueToString(stat * 100.0, 2)
    ).styled { it.withColor(getColor(stat)) }
}