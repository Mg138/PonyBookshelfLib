package io.github.mg138.bookshelf.item.type

import io.github.mg138.bookshelf.damage.DamageManager
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.projectile.ProjectileUtil
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity

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
                player, start, end, box, { !it.isInvulnerable && !it.isInvisible }, rangeSquared
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