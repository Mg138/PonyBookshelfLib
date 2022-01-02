package io.github.mg138.bookshelf.entity

import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.item.StatedItem
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.data.StatMap
import io.github.mg138.bookshelf.utils.StatUtil.filterAndSort
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World

abstract class BookStatedEntity<T : BookStatedEntity<T>>(
    type: EntityType<T>, world: World
) : BookEntity<T>(type, world) {
    abstract fun getStatMap(): StatMap

    fun getStatResult(type: StatType) =
        getStatMap().getStatResult(type)

    fun getStat(type: StatType) =
        getStatMap().getStat(type)

    fun types() =
        getStatMap().types()

    fun stats() =
        getStatMap().stats()

    fun pairs() =
        getStatMap().pairs()

    fun onBeingAttacked(
        damager: LivingEntity,
        world: World,
        hand: Hand,
        hitResult: EntityHitResult?
    ): ActionResult {
        val sortedMap = getStatMap().filterAndSort<StatEvent.OnDamageCallback> { it.onDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.onDamage(
                StatEvent.OnDamageCallback.OnDamageEvent(stat, this.getStatMap(), damager, this, world, hand, hitResult)
            )

            if (result != ActionResult.PASS) break
        }

        return ActionResult.PASS
    }

    fun afterBeingAttacked(
        damager: LivingEntity,
        item: StatedItem
    ): ActionResult {
        val sortedMap = getStatMap().filterAndSort<StatEvent.AfterDamageCallback> { it.afterDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.afterDamage(
                StatEvent.AfterDamageCallback.AfterDamageEvent(stat, this.getStatMap(), damager, this)
            )

            if (result != ActionResult.PASS) break
        }

        return ActionResult.PASS
    }
}