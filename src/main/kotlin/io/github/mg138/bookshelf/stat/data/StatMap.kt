package io.github.mg138.bookshelf.stat.data

import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.LoredStatType
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.type.StatTypeManager
import net.minecraft.util.Identifier

@Suppress("UNUSED")
open class StatMap(
    private val map: MutableMap<StatType, Stat> = defaultMap()
) : MutableStats {
    companion object {
        val EMPTY
            get() = StatMap()

        fun defaultMap(): MutableMap<StatType, Stat> = mutableMapOf()
    }

    constructor(stats: StatMap) : this(stats.map)

    fun filterKeys(predicate: (StatType) -> Boolean) = map.filterKeys(predicate).toMutableMap()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> filterType() = filterKeys { it is T } as MutableMap<T, Stat>

    open fun toMap() = defaultMap().also { it.putAll(this) }

    open fun computeIfPresent(type: StatType, action: (StatType, Stat) -> Stat) =
        this[type]?.let { old ->
            action(type, old)
                .also { this[type] = it }
        }

    open fun computeIfAbsent(type: StatType, action: (StatType) -> Stat) =
        this[type] ?: action(type)
            .also { this[type] = it }

    open fun computeIfPresent(type: StatType, action: (Stat) -> Stat) =
        this[type]?.let { old ->
            action(old)
                .also { this[type] = it }
        }

    open fun computeIfAbsent(type: StatType, action: () -> Stat) =
        this[type] ?: action()
            .also { this[type] = it }

    open fun containsType(type: StatType) = this.types().contains(type)
    open fun isEmpty() = map.isEmpty()

    open operator fun get(type: StatType) = this.getStat(type)
    open operator fun set(type: StatType, stat: Stat) = this.putStat(type, stat)

    // Stated

    override fun getStatResult(type: StatType) = this.getStat(type)?.result() ?: 0.0
    override fun getStat(type: StatType) = map[type]
    override fun types(): Set<StatType> = map.keys
    override fun stats(): Collection<Stat> = map.values
    override fun pairs(): List<Pair<StatType, Stat>> = map.entries.map { it.toPair() }
    override fun iterator() = this.pairs().iterator()
    override fun lores() = StatTypeManager.registeredTypes
        .filter { this.containsType(it) }
        .filterIsInstance<LoredStatType>()
        .map { it.lore(this[it]!!) }


    // MutableStated

    override fun addStat(type: StatType, stat: Stat) =
        stat.plus(map[type])
            .also { this[type] = it }

    override fun putStat(stat: Pair<StatType, Stat>) = this.putStat(stat.first, stat.second)
    override fun putStat(type: StatType, stat: Stat) = map.put(type, stat)
    fun putStat(id: Identifier, stat: Stat) = StatTypeManager[id]?.let { putStat(it, stat) }
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