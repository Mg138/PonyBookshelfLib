package io.github.mg138.bookshelf.item

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.Stated
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.type.event.AfterDamageListener
import io.github.mg138.bookshelf.stat.type.event.OnDamageListener
import io.github.mg138.bookshelf.stat.utils.StatMap
import net.minecraft.entity.LivingEntity
import net.minecraft.item.Item
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World


abstract class BookStatedItem(
    id: Identifier,
    bookItemSettings: BookItemSettings,
    settings: Settings, vanillaItem: Item,
    private val statMap: StatMap
) : BookItem(id, bookItemSettings, settings, vanillaItem), Stated {
    override fun getStatResult(type: StatType) = statMap.getStatResult(type)
    override fun getStat(type: StatType) = statMap.getStat(type)
    override fun stats() = statMap.stats()
    override fun types() = statMap.types()
    override fun iterator() = statMap.iterator()

    fun onAttackEntity(
        damager: LivingEntity,
        world: World,
        hand: Hand,
        damagee: LivingEntity,
        hitResult: EntityHitResult?
    ): ActionResult {
        val sortedMap = statMap
            .filterType<OnDamageListener>()
            .toSortedMap { type, _ -> type.onDamagePriority }
            .onEach { (type, _) -> println((type as StatType).id) }

        for ((type, stat) in sortedMap) {
            val result = type.onDamage(
                OnDamageListener.OnDamageEvent(stat, this, damager, damagee, world, hand, hitResult)
            )

            if (result != ActionResult.PASS) break
        }
        DamageManager.resolveDamage(damagee, damager)

        return ActionResult.PASS
    }

    fun afterAttackEntity(
        damager: LivingEntity,
        damagee: LivingEntity
    ): ActionResult {
        val sortedMap = statMap
            .filterType<AfterDamageListener>()
            .toSortedMap { type, _ -> type.afterDamagePriority }
            .onEach { (type, _) -> println((type as StatType).id) }

        for ((type, stat) in sortedMap) {
            val result = type.afterDamage(
                AfterDamageListener.AfterDamageEvent(stat, this, damager, damagee)
            )

            if (result != ActionResult.PASS) break
        }

        return ActionResult.PASS
    }
}