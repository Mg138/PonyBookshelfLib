package io.github.mg138.bookshelf.utils

import net.minecraft.text.LiteralText
import net.minecraft.text.TextColor

fun String.toLiteralText() = LiteralText(this)

fun percentColor(percent: Double = 0.0): TextColor {
    if (percent >= 0.99) {
        return TextColor.fromRgb(0x5bffec)
    }
    if (percent >= 0.9) {
        return TextColor.fromRgb(0x5bff85)
    }
    if (percent >= 0.6) {
        return TextColor.fromRgb(0xfdff59)
    }
    if (percent >= 0.3) {
        return TextColor.fromRgb(0xff8c59)
    }
    return TextColor.fromRgb(0xff5963)
}