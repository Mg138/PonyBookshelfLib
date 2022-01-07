package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.damage.DamageEvent.AfterBookDamageCallback
import io.github.mg138.bookshelf.damage.DamageEvent.OnBookDamageCallback
import io.github.mg138.bookshelf.item.type.StatedItem
import io.github.mg138.bookshelf.stat.type.StatType
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult

object DamageEvent {
    fun interface OnBookDamageCallback {
        data class OnBookDamageEvent(
            val damager: LivingEntity,
            val damagee: LivingEntity?,
            val items: Map<ItemStack, StatedItem>
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
            val damager: LivingEntity?,
            val damagee: LivingEntity?,
            val items: Map<ItemStack, StatedItem>,
            val damages: Map<StatType, Double>
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