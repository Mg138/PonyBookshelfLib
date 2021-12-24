package io.github.mg138.bookshelf.stat.utils

import io.github.mg138.bookshelf.stat.stat.Stat
import kotlin.math.max

object StatUtil {
    private fun percent(m: Double, k: Int) = max(m / (m + k), 0.0)

    private fun positivePercent(m: Double, k: Int) = 1 + percent(m, k)

    private fun negativePercent(m: Double, k: Int) = 1 - percent(m, k)

    /**
     * @param ignoreNegative only applies the modifier when it's > 0
     * @return
     * - modifier > 0: stat * modifier
     * - modifier < 0: stat gets smaller, but limited at 0 (never goes under 0)
     * - modifier = 0: ignored
     */
    private fun positiveModifier(
        stat: Stat,
        modifier: Double,
        constant: Int,
        ignoreNegative: Boolean = false
    ): Stat {
        if (modifier > 0.0) return stat * modifier

        if (modifier == 0.0 || ignoreNegative) return stat

        return stat * negativePercent(modifier, constant)
    }

    /**
     * @param ignoreNegative only applies the modifier when it's > 0
     * @return
     * - modifier > 0: stat gets smaller, but limited at 0 (never goes under 0)
     * - modifier < 0: stat gets bigger, but limited at 2x
     * - modifier = 0: ignored
     */
    private fun negativeModifier(
        stat: Stat,
        modifier: Double,
        constant: Int,
        ignoreNegative: Boolean = false
    ): Stat {
        if (modifier > 0.0) return stat * negativePercent(modifier, constant)

        if (modifier == 0.0 || ignoreNegative) return stat

        return stat * positivePercent(modifier, constant)
    }

    fun calculateTrueDamage(damage: Stat, defense: Stat) =
        (damage - defense).ensureAtLeast(0.0)

    fun damageModifier(damage: Stat, modifier: Stat, ignoreNegative: Boolean = false) =
        positiveModifier(damage, modifier.result(), 2, ignoreNegative)

    fun defense(damage: Stat, defense: Stat, ignoreNegative: Boolean = false) =
        negativeModifier(damage, defense.result(), 100, ignoreNegative) - damage
}