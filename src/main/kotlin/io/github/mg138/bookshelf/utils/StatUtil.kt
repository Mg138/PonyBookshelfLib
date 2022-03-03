package io.github.mg138.bookshelf.utils

import io.github.mg138.bookshelf.entity.StatedEntity
import io.github.mg138.bookshelf.stat.data.Stats
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.type.StatTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.util.ActionResult
import kotlin.math.max

fun StatedEntity.maxHealthStat() = this.getStat(StatTypes.MiscTypes.MaxHealth)
fun StatedEntity.healthStat() = this.getStat(StatTypes.MiscTypes.Health)

object StatUtil {
    inline fun <reified T> Iterable<Pair<StatType, Stat>>.filterAndSort(crossinline sortBy: (T) -> Int): List<Pair<T, Stat>> {
        val map = this.toMap()

        return this.asSequence()
            .map { it.first }
            .filterIsInstance<T>()
            .sortedBy { sortBy(it) }
            .map { it to map[it as StatType]!! }
            .toList()
    }

    private fun Iterable<Pair<StatEvent.OnDamageCallback, Stat>>.onDamageLoop(
        damagee: LivingEntity,
        damageeStats: Stats?,
        damager: Entity?,
        damagerStats: Stats?,
        source: DamageSource? = DamageSource.GENERIC
    ): ActionResult {
        this.forEach { (type, stat) ->
            val result = type.onDamage(
                StatEvent.OnDamageCallback.OnDamageEvent(
                    stat, damagee, damageeStats, damager, damagerStats, source
                )
            )
            if (result != ActionResult.PASS) return result
        }

        return ActionResult.PASS
    }

    fun onDamage(
        damagee: LivingEntity,
        damageeStats: Stats?,
        damager: Entity?,
        damagerStats: Stats?,
        source: DamageSource? = DamageSource.GENERIC
    ): ActionResult {
        damagerStats
            ?.filter { it.first is StatEvent.OffensiveStat }
            ?.filterAndSort<StatEvent.OnDamageCallback> { it.onDamagePriority }
            ?.onDamageLoop(damagee, damageeStats, damager, damagerStats, source)
            .takeIf { it != ActionResult.PASS }
            ?.run { return this }

        damageeStats
            ?.filter { it.first is StatEvent.DefensiveStat }
            ?.filterAndSort<StatEvent.OnDamageCallback> { it.onDamagePriority }
            ?.onDamageLoop(damagee, damageeStats, damager, damagerStats, source)
            .takeIf { it != ActionResult.PASS }
            ?.run { return this }


        return ActionResult.PASS
    }

    private fun Iterable<Pair<StatEvent.AfterDamageCallback, Stat>>.afterDamageLoop(
        damagee: LivingEntity,
        damageeStats: Stats?,
        damager: Entity?,
        damagerStats: Stats?,
        damageResults: Map<StatType, Double>,
        source: DamageSource? = DamageSource.GENERIC
    ): ActionResult {
        this.forEach { (type, stat) ->
            val result = type.afterDamage(
                StatEvent.AfterDamageCallback.AfterDamageEvent(
                    stat, damagee, damageeStats, damager, damagerStats, damageResults, source
                )
            )
            if (result != ActionResult.PASS) return result
        }

        return ActionResult.PASS
    }

    fun afterDamage(
        damagee: LivingEntity,
        damageeStats: Stats?,
        damager: Entity?,
        damagerStats: Stats?,
        damageResults: Map<StatType, Double>,
        source: DamageSource? = DamageSource.GENERIC
    ): ActionResult {
        damagerStats
            ?.filter { it.first is StatEvent.OffensiveStat }
            ?.filterAndSort<StatEvent.AfterDamageCallback> { it.afterDamagePriority }
            ?.afterDamageLoop(damagee, damageeStats, damager, damagerStats, damageResults, source)
            .takeIf { it != ActionResult.PASS }
            ?.run { return this }

        damageeStats
            ?.filter { it.first is StatEvent.DefensiveStat }
            ?.filterAndSort<StatEvent.AfterDamageCallback> { it.afterDamagePriority }
            ?.afterDamageLoop(damagee, damageeStats, damager, damagerStats, damageResults, source)
            .takeIf { it != ActionResult.PASS }
            ?.run { return this }

        return ActionResult.PASS
    }

    fun mod(a: Double, m: Double): Double {
        if (a < 0.0) {
            return a / m
        }
        return a * m
    }

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
        constant: Int = 1,
        ignoreNegative: Boolean = false
    ): Stat {
        if (modifier > 0.0) return stat * modifier

        if (modifier < 0.0) {
            if (!ignoreNegative) return stat * negativePercent(-modifier, constant)
        }

        return stat
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
        constant: Int = 1,
        ignoreNegative: Boolean = false
    ): Stat {
        if (modifier > 0.0) return stat * negativePercent(modifier, constant)

        if (modifier < 0.0) {
            if (!ignoreNegative) return stat * positivePercent(-modifier, constant)
        }

        return stat
    }

    fun calculateTrueDamage(damage: Stat, defense: Stat) =
        (damage - defense).ensureAtLeast(0.0)

    fun damageModifier(damage: Stat, modifier: Stat, ignoreNegative: Boolean = false) =
        positiveModifier(damage, modifier.result(), 2, ignoreNegative)

    fun defense(damage: Stat, defense: Stat, ignoreNegative: Boolean = false) =
        negativeModifier(damage, defense.result(), 100, ignoreNegative)
}