package io.github.mg138.bookshelf.projectile

import eu.pb4.polymer.api.entity.PolymerEntity
import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.item.type.StatedItem
import io.github.mg138.bookshelf.utils.EntityUtil.canHit
import io.github.mg138.bookshelf.utils.minus
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.projectile.ArrowEntity
import net.minecraft.entity.projectile.thrown.ThrownEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class ArrowProjectile : ThrownEntity, PolymerEntity {
    override fun getPolymerEntityType(): EntityType<ArrowEntity> = EntityType.ARROW

    constructor(type: EntityType<ArrowProjectile>, world: World)
            : super(type, world)

    constructor(owner: LivingEntity, world: World)
            : super(ARROW_PROJECTILE, owner, world)

    var itemStack: ItemStack? = null

    override fun initDataTracker() {
    }

    override fun setVelocity(velocity: Vec3d) {
        super.setVelocity(velocity)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        this.discard()
    }

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        val item = itemStack?.item as? StatedItem ?: return

        (owner as? LivingEntity)?.let { damager ->
            val damagee = entityHitResult.entity

            if (damagee is LivingEntity) {
                DamageManager.attack(damager, item.getStats(itemStack), damagee)

                this.discard()
            }
        }
    }

    override fun canHit(entity: Entity): Boolean {
        return super.canHit(entity) && entity.canHit()
    }

    companion object {
        val ARROW_PROJECTILE: EntityType<ArrowProjectile> = Registry.register(
            Registry.ENTITY_TYPE,
            Main.modId - "arrow_projectile",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::ArrowProjectile)
                .trackRangeBlocks(4).trackedUpdateRate(10)
                .dimensions(EntityType.ARROW.dimensions)
                .disableSaving()
                .build()
        )

        fun register() {
        }
    }
}