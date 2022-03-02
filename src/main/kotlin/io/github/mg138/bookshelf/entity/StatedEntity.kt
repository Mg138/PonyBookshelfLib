package io.github.mg138.bookshelf.entity

import io.github.mg138.bookshelf.stat.data.MutableStats
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType

interface StatedEntity {
    fun getStats(): MutableStats

    fun getStatResult(type: StatType): Double {
        return getStats().getStatResult(type)
    }

    fun getStat(type: StatType): Stat? {
        return getStats().getStat(type)
    }

    fun types(): Set<StatType> {
        return getStats().types()
    }

    fun stats(): Collection<Stat> {
        return getStats().stats()
    }

    fun pairs(): List<Pair<StatType, Stat>> {
        return getStats().pairs()
    }
}