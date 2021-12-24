package io.github.mg138.bookshelf.stat.type.event

import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.stat.stat.Stat
import net.minecraft.entity.LivingEntity
import net.minecraft.util.ActionResult

interface AfterDamageListener {
    data class AfterDamageEvent(
        val stat: Stat,
        val item: BookStatedItem,
        val damager: LivingEntity,
        val damagee: LivingEntity
    )

    val afterDamagePriority: Int

    fun afterDamage(event: AfterDamageEvent): ActionResult
}