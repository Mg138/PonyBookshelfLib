package io.github.mg138.bookshelf.projectile

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.utils.EntityUtil.canHit
import io.github.mg138.bookshelf.utils.minus
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.*
import net.minecraft.entity.projectile.thrown.ThrownEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class LongWandProjectile : ThrownEntity {
    constructor(type: EntityType<LongWandProjectile>, world: World)
            : super(type, world)

    constructor(owner: LivingEntity, world: World)
            : super(LONG_WAND_PROJECTILE, owner, world)

    var itemStack: ItemStack? = null

    override fun initDataTracker() {
    }

    override fun tick() {
        super.tick()
        WandProjectile.spawnParticles(this)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        this.discard()
    }

    private val map: MutableMap<LivingEntity, Int> = mutableMapOf()

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        WandProjectile.onEntityHit(itemStack, owner, map, entityHitResult)
    }


    override fun canHit(entity: Entity): Boolean {
        return super.canHit(entity) && entity.canHit()
    }

    override fun getGravity() = WandProjectile.GRAVITY

    override fun shouldRender(distance: Double) = false

    companion object {
        val LONG_WAND_PROJECTILE: EntityType<LongWandProjectile> = Registry.register(
            Registry.ENTITY_TYPE,
            Main.modId - "long_wand_projectile",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::LongWandProjectile)
                .trackRangeBlocks(0).trackedUpdateRate(10)
                .dimensions(EntityDimensions.fixed(1.0F, 1.0F))
                .disableSaving()
                .build()
        )

        fun register() {
        }
    }
}