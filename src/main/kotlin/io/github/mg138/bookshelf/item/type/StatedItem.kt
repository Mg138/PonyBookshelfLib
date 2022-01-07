package io.github.mg138.bookshelf.item.type

import io.github.mg138.bookshelf.stat.data.StatMap
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.utils.StatUtil.filterAndSort
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult

interface StatedItem {
    fun getStatMap(itemStack: ItemStack?): StatMap

    fun getStatResult(type: StatType, itemStack: ItemStack?) =
        getStatMap(itemStack).getStatResult(type)

    fun getStat(type: StatType, itemStack: ItemStack?) =
        getStatMap(itemStack).getStat(type)

    fun types(itemStack: ItemStack?) =
        getStatMap(itemStack).types()

    fun stats(itemStack: ItemStack?) =
        getStatMap(itemStack).stats()

    fun pairs(itemStack: ItemStack?) =
        getStatMap(itemStack).pairs()

    fun onAttackEntity(
        itemStack: ItemStack,
        damager: LivingEntity,
        damagee: LivingEntity?
    ): ActionResult {
        val sortedMap = pairs(itemStack).filterAndSort<StatEvent.OnDamageCallback> { it.onDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.onDamage(
                StatEvent.OnDamageCallback.OnDamageEvent(stat, this.getStatMap(itemStack), damager, damagee)
            )

            if (result != ActionResult.PASS) return result
        }

        return ActionResult.PASS
    }

    fun afterAttackEntity(
        itemStack: ItemStack?,
        damager: LivingEntity,
        damagee: LivingEntity?
    ): ActionResult {
        val sortedMap = pairs(itemStack).filterAndSort<StatEvent.AfterDamageCallback> { it.afterDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.afterDamage(
                StatEvent.AfterDamageCallback.AfterDamageEvent(stat, this.getStatMap(itemStack), damager, damagee)
            )

            if (result != ActionResult.PASS) return result
        }

        return ActionResult.PASS
    }
}