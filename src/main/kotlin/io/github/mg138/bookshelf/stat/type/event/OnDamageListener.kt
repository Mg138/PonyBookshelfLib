package io.github.mg138.bookshelf.stat.type.event

import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.stat.stat.Stat
import net.minecraft.entity.LivingEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World

interface OnDamageListener {
    data class OnDamageEvent(
        val stat: Stat,
        val item: BookStatedItem,
        val damager: LivingEntity,
        val damagee: LivingEntity,
        val world: World,
        val hand: Hand,
        val hitResult: EntityHitResult?
    )

    val onDamagePriority: Int

    fun onDamage(event: OnDamageEvent): ActionResult
}