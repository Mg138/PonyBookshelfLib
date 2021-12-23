package io.github.mg138.bookshelf.damage

import io.github.mg138.bookshelf.stat.type.StatType
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object DamageIndicatorManager {
    class Indicator(
        damage: Double, type: StatType,
        world: World, x: Double, y: Double, z: Double
    ) : ArmorStandEntity(world, x, y, z) {
        override fun isMarker() = true

        init {
            noClip = true
            isInvisible = true
            isInvulnerable = true
            isCustomNameVisible = true

            customName = type.indicator(damage.toInt())

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

    fun displayDamage(damage: Double, type: StatType, on: Entity) {
        val world = on.world
        val random = Random().nextDouble()
        val r = random * Math.PI * 2

        val x = on.x + cos(r) / 2
        val y = on.y + random / 2 + on.heightOffset - 1
        val z = on.z + sin(r) / 2

        val xV = cos(r) / 6
        val yV = 0.1
        val zV = sin(r) / 6

        val indicator = Indicator(damage, type, world, x, y, z)
        indicator.velocity = Vec3d(xV, yV, zV)
        indicators += indicator

        world.spawnEntity(indicator)
    }
}