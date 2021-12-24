package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.util.Identifier
import java.util.*

abstract class ChanceType(id: Identifier) :
    StatType(id) {
    companion object {
        private val rand = Random()

        private fun roll(chance: Double): Boolean = rand.nextDouble() <= (chance % 1)

        private fun roundChance(chance: Double) = chance.toInt().let {
            if (roll(chance)) {
                it + 1
            } else {
                it
            }
        }

        fun calculate(chance: Stat, power: Stat): Stat {
            val p = power.result()
            if (p <= 0.0) return Stat.EMPTY
            // power should always be positive
            // otherwise it shouldn't have an effect at all

            val modifier = roundChance(chance.result())
            if (modifier < 1) return Stat.EMPTY
            // chance should always be >= 1
            // if you have -1 chance or 0 chance
            // then you definitely shouldn't activate the power.

            return StatSingle(p * modifier)
        }
    }
}