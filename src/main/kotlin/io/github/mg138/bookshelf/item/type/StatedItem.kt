package io.github.mg138.bookshelf.item.type

import io.github.mg138.bookshelf.stat.data.Stats
import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.item.ItemStack

interface StatedItem {
    fun getStats(itemStack: ItemStack?): Stats

    fun getStatResult(type: StatType, itemStack: ItemStack?) =
        getStats(itemStack).getStatResult(type)

    fun getStat(type: StatType, itemStack: ItemStack?) =
        getStats(itemStack).getStat(type)

    fun types(itemStack: ItemStack?) =
        getStats(itemStack).types()

    fun stats(itemStack: ItemStack?) =
        getStats(itemStack).stats()

    fun pairs(itemStack: ItemStack?) =
        getStats(itemStack).pairs()

    /*
    fun onAttackEntity(
        itemStack: ItemStack,
        damager: LivingEntity,
        damagee: LivingEntity?
    ): ActionResult {
        val sortedMap = pairs(itemStack).filterAndSort<StatEvent.OnDamageCallback> { it.onDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.onDamage(
                StatEvent.OnDamageCallback.OnDamageEvent(stat, this.getStats(itemStack), damager, damagee, DamageSource.mob(damager))
            )

            if (result != ActionResult.PASS) return result
        }

        return ActionResult.PASS
    }

    fun afterAttackEntity(
        itemStack: ItemStack?,
        damager: LivingEntity,
        damagee: LivingEntity?,
        source: DamageSource?
    ): ActionResult {
        val sortedMap = pairs(itemStack).filterAndSort<StatEvent.AfterDamageCallback> { it.afterDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.afterDamage(
                StatEvent.AfterDamageCallback.AfterDamageEvent(stat, damagee, source)
            )

            if (result != ActionResult.PASS) return result
        }

        return ActionResult.PASS
    }
     */
}