package io.github.mg138.bookshelf.stat.type

import io.github.mg138.bookshelf.stat.stat.Stat
import net.minecraft.text.Text

interface HasLore {
    fun lore(stat: Stat): Text
}