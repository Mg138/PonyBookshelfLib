package io.github.mg138.bookshelf.effect

import net.minecraft.util.Identifier

object StatusEffectManager {
    private val map: MutableMap<Identifier, BookStatusEffect> = mutableMapOf()

    fun register(id: Identifier, bookStatusEffect: BookStatusEffect) {
        map[id] = bookStatusEffect
    }

    fun get(id: Identifier) = map[id]

    val effects
        get() = map.toList()
}