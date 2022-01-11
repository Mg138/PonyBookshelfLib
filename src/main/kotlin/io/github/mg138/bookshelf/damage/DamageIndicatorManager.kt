package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.LoredStatType
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.utils.EntityUtil.getDisplayPos
import io.github.mg138.bookshelf.utils.minus
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.mob.MobEntity.createMobAttributes
import net.minecraft.text.LiteralText
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object DamageIndicatorManager {
    private val random = Random()
    private const val TWO_PI = Math.PI * 2.0

    private val indicators: MutableList<Indicator> = mutableListOf()

    val INDICATOR: EntityType<Indicator> = Registry.register(
        Registry.ENTITY_TYPE,
        Main.modId - "indicator",
        FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::Indicator)
            .dimensions(EntityDimensions.fixed(0.0F, 0.0F))
            .disableSaving()
            .build()
    )

    class Indicator : ArmorStandEntity {
        constructor(
            entityType: EntityType<Indicator>, world: World
        ) : super(entityType, world) {
            customName = LiteralText.EMPTY
        }

        constructor(
            damage: Double, type: LoredStatType, world: World
        ) : super(INDICATOR, world) {
            customName = type.indicator(StatSingle(damage))
        }

        init {
            isMarker = true
            noClip = true
            isInvisible = true
            isInvulnerable = true
            isCustomNameVisible = true

            setNoGravity(true)
        }
    }

    fun register() {
        FabricDefaultAttributeRegistry.register(INDICATOR, createMobAttributes())

        ServerLifecycleEvents.SERVER_STOPPING.register {
            if (indicators.isNotEmpty()) {
                indicators.forEach { it.kill() }
                indicators.clear()
            }
        }

        ServerTickEvents.END_WORLD_TICK.register {
            val iter = indicators.iterator()

            while (iter.hasNext()) {
                val indicator = iter.next()
                if (indicator.age >= 40) {
                    indicator.kill()
                    iter.remove()
                }
            }
        }
    }

    fun displayDamage(damage: Double, type: StatType, entity: Entity) {
        if (type !is LoredStatType) return

        val world = entity.world
        val pos = entity.getDisplayPos()

        val rX = random.nextDouble() * TWO_PI
        val rZ = random.nextDouble() * TWO_PI

        val x = pos.x + cos(rX) / 2.0
        val y = pos.y + random.nextDouble() - 0.2
        val z = pos.z + sin(rZ) / 2.0

        val xV = cos(rX) / 6.0
        val yV = 0.15
        val zV = sin(rZ) / 6.0

        val indicator = Indicator(damage, type, world)
        indicator.teleport(x, y, z)
        indicator.velocity = Vec3d(xV, yV, zV)
        indicators += indicator

        world.spawnEntity(indicator)
    }
}