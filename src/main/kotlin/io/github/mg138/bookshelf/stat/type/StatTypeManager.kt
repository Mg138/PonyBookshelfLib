package io.github.mg138.bookshelf.stat.type

import net.minecraft.util.Identifier

object StatTypeManager {
    private val map: MutableMap<Identifier, StatType> = mutableMapOf()
    private val types: MutableList<StatType> = mutableListOf()

    val registeredTypes: List<StatType>
        get() = types

    operator fun get(id: Identifier) = map[id]

    fun register(type: StatType) {
        val id = type.id
        if (type in types) throw IllegalArgumentException("Duplicate StatType of identifier $id!")

        types += type
        map[id] = type
    }

    operator fun plusAssign(type: StatType) = register(type)
}