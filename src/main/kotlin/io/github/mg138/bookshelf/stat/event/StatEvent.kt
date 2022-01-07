package io.github.mg138.bookshelf.stat.event

import io.github.mg138.bookshelf.item.type.StatedItem
import io.github.mg138.bookshelf.stat.data.Stats
import io.github.mg138.bookshelf.stat.stat.Stat
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult

class StatEvent {
    fun interface OnItemPostProcessCallback {
        data class ItemPostProcessEvent(
            val stat: Stat,
            val item: StatedItem,
            val itemStack: ItemStack
        )

        val postProcessPriority: Int
            get() = 10

        fun onPostProcess(event: ItemPostProcessEvent): ActionResult
    }

    fun interface OnDamageCallback {
        data class OnDamageEvent(
            val stat: Stat,
            val stats: Stats,
            val damager: LivingEntity,
            val damagee: LivingEntity?
        )

        val onDamagePriority: Int
            get() = 10

        fun onDamage(event: OnDamageEvent): ActionResult
    }

    fun interface AfterDamageCallback {
        data class AfterDamageEvent(
            val stat: Stat,
            val stats: Stats,
            val damager: LivingEntity,
            val damagee: LivingEntity?
        )

        val afterDamagePriority: Int
            get() = 10

        fun afterDamage(event: AfterDamageEvent): ActionResult
    }
}