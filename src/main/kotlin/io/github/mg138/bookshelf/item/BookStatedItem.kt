package io.github.mg138.bookshelf.item

import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.StatMap
import io.github.mg138.bookshelf.utils.StatUtil.filterAndSort
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World


abstract class BookStatedItem(
    id: Identifier,
    bookItemSettings: BookItemSettings,
    settings: Settings, vanillaItem: Item
) : BookItem(id, bookItemSettings, settings, vanillaItem) {
    abstract fun getStatMap(itemStack: ItemStack?): StatMap

    fun getStatResult(type: StatType, itemStack: ItemStack? = null) =
        getStatMap(itemStack).getStatResult(type)

    fun getStat(type: StatType, itemStack: ItemStack? = null) =
        getStatMap(itemStack).getStat(type)

    fun types(itemStack: ItemStack? = null) =
        getStatMap(itemStack).types()

    fun stats(itemStack: ItemStack? = null) =
        getStatMap(itemStack).stats()

    fun pairs(itemStack: ItemStack? = null) =
        getStatMap(itemStack).pairs()

    fun iterator(itemStack: ItemStack? = null) =
        getStatMap(itemStack).iterator()

    override fun appendTooltip(itemStack: ItemStack, world: World?, tooltip: MutableList<Text>, context: TooltipContext) {
        super.appendTooltip(itemStack, world, tooltip, context)
        tooltip.addAll(getStatMap(itemStack).lores())
    }

    fun onAttackEntity(
        itemStack: ItemStack,
        damager: LivingEntity,
        hand: Hand,
        damagee: LivingEntity,
        hitResult: EntityHitResult?,
        world: World
    ): ActionResult {
        val sortedMap = pairs(itemStack).filterAndSort<StatEvent.OnDamageCallback> { it.onDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.onDamage(
                StatEvent.OnDamageCallback.OnDamageEvent(stat, this, damager, damagee, world, hand, hitResult)
            )

            if (result != ActionResult.PASS) break
        }

        return ActionResult.PASS
    }

    fun afterAttackEntity(
        itemStack: ItemStack?,
        damager: LivingEntity,
        damagee: LivingEntity
    ): ActionResult {
        val sortedMap = pairs(itemStack).filterAndSort<StatEvent.AfterDamageCallback> { it.afterDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.afterDamage(
                StatEvent.AfterDamageCallback.AfterDamageEvent(stat, this, damager, damagee)
            )

            if (result != ActionResult.PASS) break
        }

        return ActionResult.PASS
    }
}