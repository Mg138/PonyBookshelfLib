package io.github.mg138.bookshelf.stat.utils

import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.Preset
import io.github.mg138.bookshelf.stat.type.StatType
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
    fun positiveModifier(
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
     * - modifier > 0: stat * modifier
     * - modifier < 0: stat gets smaller, but limited at 0 (never goes under 0)
     * - modifier = 0: ignored
     */
    fun positiveModifier(
        stat: Double,
        modifier: Double,
        constant: Int,
        ignoreNegative: Boolean = false
    ): Double {
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
    fun negativeModifier(
        stat: Stat,
        modifier: Double,
        constant: Int,
        ignoreNegative: Boolean = false
    ): Stat {
        if (modifier > 0.0) return stat * negativePercent(modifier, constant)

        if (modifier == 0.0 || ignoreNegative) return stat

        return stat * positivePercent(modifier, constant)
    }

    /**
     * @param ignoreNegative only applies the modifier when it's > 0
     * @return
     * - modifier > 0: stat gets smaller, but limited at 0 (never goes under 0)
     * - modifier < 0: stat gets bigger, but limited at 2x
     * - modifier = 0: ignored
     */
    fun negativeModifier(
        stat: Double,
        modifier: Double,
        constant: Int,
        ignoreNegative: Boolean = false
    ): Double {
        if (modifier > 0.0) return stat * negativePercent(modifier, constant)

        if (modifier == 0.0 || ignoreNegative) return stat

        return stat * positivePercent(modifier, constant)
    }

    /**
     * Calculates the damage *specifically* for [Preset.DamageTypes.DAMAGE_TRUE]
     * @return The calculated True Damage, cannot be less than 0
     */
    fun calculateTrueDamage(damage: Stat, defense: Stat): Stat {
        return (damage - defense).ensureAtLeast(0.0)
    }

    fun damageModifier(damage: Stat, modifier: Double, ignoreNegative: Boolean = false) =
        positiveModifier(damage, modifier, 2, ignoreNegative)

    fun damageModifier(damage: Double, modifier: Double, ignoreNegative: Boolean = false) =
        positiveModifier(damage, modifier, 2, ignoreNegative)

    fun defense(damage: Stat, defense: Double, ignoreNegative: Boolean = false) =
        negativeModifier(damage, defense, 100, ignoreNegative)

    fun defense(damage: Double, defense: Double, ignoreNegative: Boolean = false) =
        negativeModifier(damage, defense, 100, ignoreNegative)
}