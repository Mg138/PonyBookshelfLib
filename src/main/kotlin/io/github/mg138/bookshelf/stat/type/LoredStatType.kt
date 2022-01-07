package io.github.mg138.bookshelf.stat.type

import io.github.mg138.bookshelf.stat.stat.Stat
import net.minecraft.text.*
import net.minecraft.util.Identifier


abstract class LoredStatType(
    id: Identifier
): StatType(id), HasLore {
    var color: TextColor = TextColor.fromRgb(0xFFFFFF)

    constructor(id: Identifier, numberColor: TextColor?) : this(id) {
        numberColor?.let {
            this.color = numberColor
        }
    }


    protected open fun valueToString(value: Stat, digitsAfterDecimal: Int = 1) =
        round(value, digitsAfterDecimal).toString()

    protected open fun valueWithColor(value: Stat, digitsAfterDecimal: Int = 1): MutableText =
        LiteralText(valueToString(value, digitsAfterDecimal))
            .styled { it.withColor(color) }

    val translationKey = "pony_bookshelf.stat_type.${id.namespace}.${id.path}"
    val iconKey = "$translationKey.icon"
    val indicatorTranslationKey = "$translationKey.indicator"

    open fun icon() = TranslatableText(iconKey)

    open fun nameText(stat: Stat): Text = TranslatableText(
        translationKey, valueToString(stat)
    ).styled { it.withColor(color) }

    open fun name(stat: Stat = Stat.EMPTY): Text = icon().append(nameText(stat))

    open fun indicatorText(stat: Stat): Text = TranslatableText(
        indicatorTranslationKey, valueToString(stat)
    ).styled { it.withColor(color) }

    open fun indicator(stat: Stat = Stat.EMPTY): Text = icon().append(indicatorText(stat))

    override fun lore(stat: Stat) = name(stat)
}