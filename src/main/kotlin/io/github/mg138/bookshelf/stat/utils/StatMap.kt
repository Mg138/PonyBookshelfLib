package io.github.mg138.bookshelf.stat.utils

import io.github.mg138.bookshelf.stat.MutableStated
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.type.StatTypeManager
import net.minecraft.util.Identifier
import java.lang.IllegalArgumentException
import kotlin.collections.HashMap

open class StatMap(private val map: MutableMap<StatType, Stat> = defaultMap()) : MutableStated {
    companion object {
        fun defaultMap(): MutableMap<StatType, Stat> = HashMap()
    }

    constructor(stats: StatMap) : this(stats.map)

    open fun toMap(): MutableMap<StatType, Stat> = HashMap<StatType, Stat>()
        .also { it.putAll(this) }

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

    override fun types() = map.keys
    override fun stats() = map.values
    override fun getStatResult(type: StatType) = this.getStat(type)?.result() ?: 0.0
    override fun getStat(type: StatType) = map[type]

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

    // Iterable

    override fun iterator() = object : Iterator<Pair<StatType, Stat>> {
        val it = this@StatMap
        val keys = it.map.keys.iterator()

        override fun hasNext() = keys.hasNext()

        override fun next() = keys.next().let { type ->
            it.getStat(type)?.let {
                Pair(type, it)
            } ?: throw IllegalArgumentException(
                """
                Error when iterating through ${it::class.simpleName}: it doesn't contain ${type.id}!
                Did the values change during iteration?
                Contents: $it
                """.trimMargin()
            )
        }
    }
}