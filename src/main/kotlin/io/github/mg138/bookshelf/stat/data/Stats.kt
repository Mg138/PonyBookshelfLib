package io.github.mg138.bookshelf.stat.data

import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.text.Text

interface Stats : Iterable<Pair<StatType, Stat>> {
    fun getStatResult(type: StatType): Double
    fun getStat(type: StatType): Stat?
    fun types(): Set<StatType>
    fun stats(): Collection<Stat>
    fun pairs(): List<Pair<StatType, Stat>>
    fun lores(): Iterable<Text>
}