package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World

object DamageEvent {
    fun interface OnBookDamageCallback {
        data class OnBookDamageEvent(
            val item: BookStatedItem,
            val damager: LivingEntity,
            val damagee: LivingEntity,
            val world: World,
            val hand: Hand,
            val hitResult: EntityHitResult?
        )

        fun onDamage(event: OnBookDamageEvent): ActionResult
    }

    val ON_BOOK_DAMAGE: Event<OnBookDamageCallback> = EventFactory.createArrayBacked(
        OnBookDamageCallback::class.java
    ) { listeners ->
        OnBookDamageCallback {
            for (listener in listeners) {
                val result: ActionResult = listener.onDamage(it)
                if (result != ActionResult.PASS) {
                    return@OnBookDamageCallback result
                }
            }
            ActionResult.PASS
        }
    }

    fun interface AfterBookDamageCallback {
        data class AfterBookDamageEvent(
            val damages: Map<StatType, Stat>,
            val item: BookStatedItem?,
            val damager: LivingEntity?,
            val damagee: LivingEntity
        )

        fun afterDamage(event: AfterBookDamageEvent): ActionResult
    }

    val AFTER_BOOK_DAMAGE: Event<AfterBookDamageCallback> = EventFactory.createArrayBacked(
        AfterBookDamageCallback::class.java
    ) { listeners ->
        AfterBookDamageCallback {
            for (listener in listeners) {
                val result: ActionResult = listener.afterDamage(it)
                if (result != ActionResult.PASS) {
                    return@AfterBookDamageCallback result
                }
            }
            ActionResult.PASS
        }
    }
}