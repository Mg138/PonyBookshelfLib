package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.LoredStatType
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.utils.EntityUtil.getDisplayPos
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object DamageIndicatorManager {
    private val random = Random()
    private const val TWO_PI = Math.PI * 2.0

    class Indicator(
        damage: Double, type: LoredStatType,
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

        val indicator = Indicator(damage, type, world, x, y, z)
        indicator.velocity = Vec3d(xV, yV, zV)
        indicators += indicator

        world.spawnEntity(indicator)
    }
}