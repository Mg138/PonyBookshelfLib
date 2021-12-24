package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

abstract class PercentageStatType(id: Identifier) : StatType(id) {
    override fun valueToString(value: Stat, digitsAfterDecimal: Int) =
        round(value, digitsAfterDecimal).toString() + "%"

    override fun name(stat: Stat) = TranslatableText(
        translationKey, valueWithColor(stat * 100.0, 2)
    )

    override fun indicator(stat: Stat) = TranslatableText(
        indicatorTranslationKey, valueWithColor(stat * 100.0, 2)
    )
}