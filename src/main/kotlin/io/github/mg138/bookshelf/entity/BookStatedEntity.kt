package io.github.mg138.bookshelf.entity

import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.stat.Stated
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.StatMap
import io.github.mg138.bookshelf.utils.StatUtil.filterAndSort
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World

abstract class BookStatedEntity<T : BookStatedEntity<T>>(
    type: EntityType<T>, world: World,
    private val statMap: StatMap
) : BookEntity<T>(type, world), Stated {
    override fun getStatResult(type: StatType) = statMap.getStatResult(type)
    override fun getStat(type: StatType) = statMap.getStat(type)
    override fun stats() = statMap.stats()
    override fun types() = statMap.types()
    override fun iterator() = statMap.iterator()

    fun onBeingAttacked(
        item: BookStatedItem,
        damager: LivingEntity,
        world: World,
        hand: Hand,
        hitResult: EntityHitResult?
    ): ActionResult {
        val sortedMap = statMap.filterAndSort<StatEvent.OnDamageCallback> { it.onDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.onDamage(
                StatEvent.OnDamageCallback.OnDamageEvent(stat, item, damager, this, world, hand, hitResult)
            )

            if (result != ActionResult.PASS) break
        }

        return ActionResult.PASS
    }

    fun afterBeingAttacked(
        damager: LivingEntity,
        item: BookStatedItem
    ): ActionResult {
        val sortedMap = statMap.filterAndSort<StatEvent.AfterDamageCallback> { it.afterDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.afterDamage(
                StatEvent.AfterDamageCallback.AfterDamageEvent(stat, item, damager, this)
            )

            if (result != ActionResult.PASS) break
        }

        return ActionResult.PASS
    }
}