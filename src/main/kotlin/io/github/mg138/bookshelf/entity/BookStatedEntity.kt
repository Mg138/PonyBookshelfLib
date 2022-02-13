package io.github.mg138.bookshelf.entity

import net.minecraft.entity.EntityType
import net.minecraft.world.World

abstract class BookStatedEntity<T : BookStatedEntity<T>>(
    type: EntityType<T>, world: World
) : BookEntity<T>(type, world), StatedEntity {
    /*
    fun onBeingAttacked(
        damager: Entity?,
        source: DamageSource?
    ): ActionResult {
        if (damager == null) return ActionResult.PASS
        if (damager !is LivingEntity) return ActionResult.PASS

        val sortedMap = getStats().filterAndSort<StatEvent.OnDamageCallback> { it.onDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.onDamage(
                StatEvent.OnDamageCallback.OnDamageEvent(
                    stat,
                    this.getStats(),
                    damager,
                    this,
                    source
                )
            )

            if (result != ActionResult.PASS) return result
        }

        return ActionResult.PASS
    }

    fun afterBeingAttacked(
        damager: LivingEntity, damageSource: DamageSource
    ): ActionResult {
        val sortedMap = getStats().filterAndSort<StatEvent.AfterDamageCallback> { it.afterDamagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.afterDamage(
                StatEvent.AfterDamageCallback.AfterDamageEvent(
                    stat,
                    this,
                    damageSource
                )
            )

            if (result != ActionResult.PASS) return result
        }

        return ActionResult.PASS
    }
     */
}