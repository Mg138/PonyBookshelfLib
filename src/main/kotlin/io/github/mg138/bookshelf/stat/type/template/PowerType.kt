package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.util.Identifier

/**
 * Doesn't listen to damage event, because it shouldn't,
 *
 * but instead it should be waiting on [ChanceType] to activate it.
 */
abstract class PowerType(id: Identifier) :
    StatType(id) {
    abstract fun onDamage(power: Double)
}