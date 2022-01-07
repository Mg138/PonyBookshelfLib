package io.github.mg138.bookshelf.stat.type

import com.google.common.math.IntMath.pow
import io.github.mg138.bookshelf.stat.stat.Stat
import net.minecraft.util.Identifier


abstract class StatType(
    val id: Identifier
) {
    fun register() {
        StatTypeManager.register(this)
    }

    // Any

    override fun toString() = this.id.toString()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is StatType) return false

        return this.id == other.id
    }

    override fun hashCode() = this.id.hashCode()

    // Translate

    protected fun round(value: Stat, digitsAfterDecimal: Int): Stat {
        val m = pow(10, digitsAfterDecimal)

        return (value * m).round() / m
    }
}