package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.utils.EntityUtil.getDisplayPos
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object DamageIndicatorManager {
    private val random = Random()

    class Indicator(
        damage: Double, type: StatType,
        world: World, x: Double, y: Double, z: Double
    ) : ArmorStandEntity(world, x, y, z) {
        init {
            isMarker = true
            noClip = true
            isInvisible = true
            isInvulnerable = true
            isCustomNameVisible = true

            customName = type.indicator(StatSingle(damage))

            setNoGravity(true)
        }
    }

    private val indicators: MutableList<Indicator> = mutableListOf()

    fun register() {
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
        val world = entity.world
        val pos = entity.getDisplayPos()

        val random = random.nextDouble()
        val r = random * Math.PI * 2.0

        val x = pos.x + cos(r) / 2.0
        val y = pos.y + random - 0.5
        val z = pos.z + sin(r) / 2.0

        val xV = cos(r) / 6.0
        val yV = 0.15
        val zV = sin(r) / 6.0

        val indicator = Indicator(damage, type, world, x, y, z)
        indicator.velocity = Vec3d(xV, yV, zV)
        indicators += indicator

        world.spawnEntity(indicator)
    }
}