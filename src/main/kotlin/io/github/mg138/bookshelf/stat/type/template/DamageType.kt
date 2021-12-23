package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.result.DamageResult
import io.github.mg138.bookshelf.result.UseOnEntityResult
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.TextColor
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World

abstract class DamageType(id: Identifier) :
    StatType(id) {
    override val damagePriority = 0

    override fun onDamage(
        stat: Stat,
        statedItem: BookStatedItem,
        player: PlayerEntity,
        world: World,
        hand: Hand,
        entity: LivingEntity,
        hitResult: EntityHitResult?
    ): UseOnEntityResult {
        return UseOnEntityResult(ActionResult.PASS, DamageResult(stat, this))
    }
}