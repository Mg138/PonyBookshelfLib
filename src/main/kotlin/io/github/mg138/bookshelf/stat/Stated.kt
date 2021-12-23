package io.github.mg138.bookshelf.stat

import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType

interface Stated : Iterable<Pair<StatType, Stat>> {
    fun types(): Set<StatType>
    fun stats(): Collection<Stat>
    fun getStatResult(type: StatType): Double
    fun getStat(type: StatType): Stat?
}