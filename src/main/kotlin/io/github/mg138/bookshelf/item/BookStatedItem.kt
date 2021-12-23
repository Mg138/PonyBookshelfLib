package io.github.mg138.bookshelf.item

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.utils.StatMap
import io.github.mg138.bookshelf.stat.Stated
import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
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

    fun onPlayerAttackEntity(
        player: PlayerEntity,
        world: World,
        hand: Hand,
        entity: Entity,
        hitResult: EntityHitResult?
    ): ActionResult {
        if (entity !is LivingEntity) return ActionResult.FAIL

        val sortedMap = statMap
            //.filter { (type, _) -> type.damagePriority != null }
            .sortedBy { (type, _) -> type.damagePriority }

        for ((type, stat) in sortedMap) {
            val result = type.onDamage(stat, this, player, world, hand, entity, hitResult) ?: continue

            if (result.actionResult != ActionResult.PASS) break

            result.damageResult?.let {
                DamageManager.queueDamage(entity, it)
            }
        }
        DamageManager.resolveDamage(entity, player)

        return ActionResult.PASS
    }
}