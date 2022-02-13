package io.github.mg138.bookshelf.stat.data

import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType

interface MutableStats : Stats {
    /**
     * Adds the stat onto pre-existing value, or put it if there wasn't one.
     * @param type the type of the stat
     * @param stat the stat to add
     *
     * @return The result of adding
     */
    fun addStat(type: StatType, stat: Stat): Stat

    fun addAll(stats: Stats?): Stats

    /**
     * Puts the stat into it.
     * @param [type] the type of the stat
     * @param [stat] the stat put in
     *
     * @return The old stat, null if none
     */
    fun putStat(type: StatType, stat: Stat): Stat?

    operator fun set(type: StatType, stat: Stat) = putStat(type, stat)

    fun putStat(stat: Pair<StatType, Stat>): Stat?

    /**
     * Removes the [Stat] of [type] from it
     *
     * @return The old stat, null if none
     */
    fun remove(type: StatType): Stat?

    fun computeIfPresent(type: StatType, action: (StatType, Stat) -> Stat) =
        this[type]?.let { old ->
            action(type, old)
                .also { this[type] = it }
        }

    fun computeIfAbsent(type: StatType, action: (StatType) -> Stat) =
        this[type] ?: action(type)
            .also { this[type] = it }

    fun computeIfPresent(type: StatType, action: (Stat) -> Stat) =
        this[type]?.let { old ->
            action(old)
                .also { this[type] = it }
        }

    fun computeIfAbsent(type: StatType, action: () -> Stat) =
        this[type] ?: action()
            .also { this[type] = it }
}