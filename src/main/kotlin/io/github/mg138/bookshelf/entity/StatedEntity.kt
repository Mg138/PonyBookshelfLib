package io.github.mg138.bookshelf.entity

import io.github.mg138.bookshelf.stat.data.MutableStats
import io.github.mg138.bookshelf.stat.type.StatType

interface StatedEntity {
    fun getStats(): MutableStats

    fun getStatResult(type: StatType) =
        getStats().getStatResult(type)

    fun getStat(type: StatType) =
        getStats().getStat(type)

    fun types() =
        getStats().types()

    fun stats() =
        getStats().stats()

    fun pairs() =
        getStats().pairs()
}