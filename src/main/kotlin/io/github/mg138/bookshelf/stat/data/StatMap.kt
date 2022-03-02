package io.github.mg138.bookshelf.stat.data

import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.LoredStatType
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.type.StatTypeManager

@Suppress("UNUSED")
open class StatMap(
    private val map: MutableMap<StatType, Stat> = defaultMap()
) : MutableStats {
    companion object {
        val EMPTY
            get() = StatMap()

        fun <K, V> defaultMap(): MutableMap<K, V> = mutableMapOf()
    }

    constructor(stats: StatMap) : this(stats.map)

    fun filterKeys(predicate: (StatType) -> Boolean) = map.filterKeys(predicate).toMutableMap()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> filterType() = filterKeys { it is T } as MutableMap<T, Stat>

    // Stated

    override fun getStatResult(type: StatType) = this.getStat(type)?.result() ?: 0.0
    override fun getStat(type: StatType) = map[type]
    override fun types(): Set<StatType> = map.keys
    override fun stats(): Collection<Stat> = map.values
    override fun pairs(): List<Pair<StatType, Stat>> = map.entries.map { it.toPair() }
    override fun iterator() = this.pairs().iterator()
    override fun lores() = StatTypeManager.registeredTypes
        .filter { this.has(it) }
        .filterIsInstance<LoredStatType>()
        .map { it.lore(this[it]!!) }

    override fun has(type: StatType): Boolean = map.containsKey(type)
    override fun copy() = StatMap().also {
        it.addAll(this)
    }

    // MutableStated

    override fun addStat(type: StatType, stat: Stat) =
        stat.plus(map[type])
            .also { this[type] = it }

    override fun addAll(stats: Stats?): Stats {
        stats?.forEach { (type, stat) -> this.addStat(type, stat) }
        return this
    }

    override fun putStat(stat: Pair<StatType, Stat>) = this.putStat(stat.first, stat.second)
    override fun putStat(type: StatType, stat: Stat) = map.put(type, stat)
    override fun remove(type: StatType) = map.remove(type)

    // Any

    override fun toString() = map.toString()
    override fun hashCode() = map.hashCode()
    override fun equals(other: Any?): Boolean {
        if (this === other) return true

        if (other !is StatMap) return false

        if (map != other.map) return false

        return true
    }
}