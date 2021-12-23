package io.github.mg138.bookshelf.stat.type

import net.minecraft.util.Identifier

object StatTypeManager {
    private val map: MutableMap<Identifier, StatType> = HashMap()

    val registeredTypes
        get() = map.values

    val cache: MutableMap<Class<*>, List<*>> = HashMap()

    inline fun <reified T : Any> filter(): List<T> {
        @Suppress("UNCHECKED_CAST")
        return cache.computeIfAbsent(T::class.java) {
            registeredTypes.filterIsInstance<T>()
        } as List<T>
    }

    operator fun get(id: Identifier) = map[id]

    fun register(type: StatType) {
        val id = type.id
        if (map.containsKey(id)) throw IllegalArgumentException("Duplicate StatType of id $id!")

        map[id] = type
    }

    operator fun plusAssign(type: StatType) = register(type)
}