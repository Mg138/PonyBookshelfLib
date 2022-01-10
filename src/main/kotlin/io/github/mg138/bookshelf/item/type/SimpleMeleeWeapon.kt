package io.github.mg138.bookshelf.item.type

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.utils.EntityUtil.canHit
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.hit.HitResult
import net.minecraft.world.RaycastContext

interface SimpleMeleeWeapon : MeleeWeapon {
    val range: Double

    override fun onLeftClick(player: ServerPlayerEntity, itemStack: ItemStack): Boolean {
        if (!super.onLeftClick(player, itemStack)) return false

        if (this is StatedItem) {
            val start = player.getCameraPosVec(1.0F)
            val end = start.add(player.getRotationVec(1.0F).multiply(range))

            val blockResult = player.world.raycast(
                RaycastContext(
                    start,
                    end,
                    RaycastContext.ShapeType.VISUAL,
                    RaycastContext.FluidHandling.NONE,
                    player
                )
            )

            val cutRange = if (blockResult.type != HitResult.Type.MISS) {
                blockResult.pos.squaredDistanceTo(player.pos)
            } else {
                range
            }

            val cutEnd = if (blockResult.type != HitResult.Type.MISS) {
                start.add(player.getRotationVec(1.0F).multiply(cutRange))
            } else {
                end
            }

            val box = player.cameraEntity.boundingBox.expand(cutRange)

            val hitResult = ProjectileUtil.raycast(
                player, start, cutEnd, box, { it.canHit() }, cutRange * cutRange
            )

            if (hitResult != null) {
                val entity = hitResult.entity

                if (entity is LivingEntity) {
                    val items: MutableMap<ItemStack, StatedItem> = mutableMapOf()

                    items[itemStack] = this
                    items.putAll(DamageManager.getArmor(player))

                    DamageManager.onPlayerAttackLivingEntity(player, entity, items)
                }
            }
        }
        return true
    }
}