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

    private val rangeSquared: Double
        get() = range * range

    override fun onLeftClick(player: ServerPlayerEntity, itemStack: ItemStack): Boolean {
        if (!super.onLeftClick(player, itemStack)) return false

        if (this is StatedItem) {
            val start = player.getCameraPosVec(1.0F)
            val end = start.add(player.getRotationVec(1.0F).multiply(range))
            val box = player.cameraEntity.boundingBox.expand(range)


            val hitResult = ProjectileUtil.raycast(
                player, start, end, box, { it.canHit() }, rangeSquared
            )
            val blockResult = player.world.raycast(
                RaycastContext(
                    start,
                    end,
                    RaycastContext.ShapeType.OUTLINE,
                    RaycastContext.FluidHandling.NONE,
                    player
                )
            )

            if (hitResult != null) {
                if (blockResult.type != HitResult.Type.MISS) {
                    if (hitResult.squaredDistanceTo(player) > blockResult.squaredDistanceTo(player)) return false
                }

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