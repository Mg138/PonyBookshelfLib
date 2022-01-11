package io.github.mg138.bookshelf.projectile

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.item.type.StatedItem
import io.github.mg138.bookshelf.utils.EntityUtil.canHit
import io.github.mg138.bookshelf.utils.ParticleUtil.spawnParticles
import io.github.mg138.bookshelf.utils.minus
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.*
import net.minecraft.entity.projectile.thrown.ThrownEntity
import net.minecraft.item.ItemStack
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.hit.EntityHitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class WandProjectile : ThrownEntity {
    constructor(type: EntityType<WandProjectile>, world: World)
            : super(type, world)

    constructor(owner: LivingEntity, world: World)
            : super(WAND_PROJECTILE, owner, world)

    var itemStack: ItemStack? = null

    override fun initDataTracker() {
    }

    override fun tick() {
        super.tick()
        spawnParticles(this)
    }

    override fun onBlockHit(blockHitResult: BlockHitResult?) {
        this.discard()
    }

    private val map: MutableMap<LivingEntity, Int> = mutableMapOf()

    override fun onEntityHit(entityHitResult: EntityHitResult) {
        Companion.onEntityHit(itemStack, owner, map, entityHitResult)
    }

    override fun canHit(entity: Entity): Boolean {
        return super.canHit(entity) && entity.canHit()
    }

    override fun getGravity() = GRAVITY

    override fun shouldRender(distance: Double) = false

    companion object {
        private const val DELAY = 3

        const val GRAVITY = 0.1F

        fun spawnParticles(entity: Entity) {
            entity.spawnParticles(ParticleTypes.CRIT, entity.pos, 4, Vec3d.ZERO, 0.0)
        }

        fun onEntityHit(itemStack: ItemStack?, owner: Entity?, map: MutableMap<LivingEntity, Int>, entityHitResult: EntityHitResult) {
            val item = itemStack?.item as? StatedItem ?: return

            (owner as? ServerPlayerEntity)?.let { player ->
                val entity = entityHitResult.entity

                if (entity is LivingEntity) {
                    val delay = map[entity] ?: 0

                    if (delay <= 0) {
                        val items: MutableMap<ItemStack, StatedItem> = mutableMapOf()

                        items[itemStack] = item
                        items.putAll(DamageManager.getArmor(player))

                        DamageManager.onPlayerAttackLivingEntity(player, entity, items)

                        map[entity] = DELAY
                    } else {
                        map.computeIfPresent(entity) { _, it -> it - 1 }
                    }
                }
            }
        }

        val WAND_PROJECTILE: EntityType<WandProjectile> = Registry.register(
            Registry.ENTITY_TYPE,
            Main.modId - "wand_projectile",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::WandProjectile)
                .trackRangeBlocks(0).trackedUpdateRate(10)
                .dimensions(EntityDimensions.fixed(1.0F, 1.0F))
                .disableSaving()
                .build()
        )

        fun register() {
        }
    }
}