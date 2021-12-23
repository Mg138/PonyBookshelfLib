package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.item.BookStatedItem
import io.github.mg138.bookshelf.result.DamageResult
import io.github.mg138.bookshelf.result.UseOnEntityResult
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.utils.StatUtil
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.world.World

abstract class DefenseType(id: Identifier) :
    StatType(id) {
    override val damagePriority = 10000000

    abstract class DefenseTypeTemplate(
        id: Identifier,
        val damageType: DamageType
    ) : DefenseType(id) {
        override fun onDamage(
            stat: Stat,
            statedItem: BookStatedItem,
            player: PlayerEntity,
            world: World,
            hand: Hand,
            entity: LivingEntity,
            hitResult: EntityHitResult?
        ): UseOnEntityResult? {
            DamageManager.get(entity)?.let {
                it[damageType]?.let { damage ->
                    return UseOnEntityResult(ActionResult.PASS, DamageResult(damageCalc(damage, stat), damageType))
                }
            }
            return super.onDamage(stat, statedItem, player, world, hand, entity, hitResult)
        }

        open fun damageCalc(damage: Stat, stat: Stat) = StatUtil.defense(damage, stat.result())
    }
}