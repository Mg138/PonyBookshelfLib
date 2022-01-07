package io.github.mg138.bookshelf.stat.type

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.effect.Bleeding
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.template.*
import io.github.mg138.bookshelf.utils.ObjectUtil
import io.github.mg138.bookshelf.utils.ParticleUtil.spawnParticles
import io.github.mg138.bookshelf.utils.StatUtil
import io.github.mg138.bookshelf.utils.minus
import net.minecraft.entity.EntityGroup
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.text.TextColor
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Vec3d
import java.lang.Double.min
import java.util.*
import kotlin.math.abs

@Suppress("UNUSED")
object StatTypes {
    object DamageTypes {
        val DAMAGE_TRUE =
            object : DamageType(Main.modId - "damage_true") {}

        const val DAMAGE_PHYSICAL_COLOR = 0xf5ab3d
        val DAMAGE_PHYSICAL =
            object : DamageType(Main.modId - "damage_physical") {
                init {
                    color = TextColor.fromRgb(DAMAGE_PHYSICAL_COLOR)
                }
            }

        const val DAMAGE_TERRA_COLOR = 0x7bb651
        val DAMAGE_TERRA =
            object : DamageType(Main.modId - "damage_terra") {
                init {
                    color = TextColor.fromRgb(DAMAGE_TERRA_COLOR)
                }
            }
        const val DAMAGE_TEMPUS_COLOR = 0xffe494
        val DAMAGE_TEMPUS =
            object : DamageType(Main.modId - "damage_tempus") {
                init {
                    color = TextColor.fromRgb(DAMAGE_TEMPUS_COLOR)
                }
            }

        const val DAMAGE_IGNIS_COLOR = 0xf8822f
        val DAMAGE_IGNIS =
            object : DamageType(Main.modId - "damage_ignis") {
                init {
                    color = TextColor.fromRgb(DAMAGE_IGNIS_COLOR)
                }
            }

        const val DAMAGE_AQUA_COLOR = 0x7291ff
        val DAMAGE_AQUA =
            object : DamageType(Main.modId - "damage_aqua") {
                init {
                    color = TextColor.fromRgb(DAMAGE_AQUA_COLOR)
                }
            }

        const val DAMAGE_LUMEN_COLOR = 0xfff46e
        val DAMAGE_LUMEN =
            object : DamageType(Main.modId - "damage_lumen") {
                init {
                    color = TextColor.fromRgb(DAMAGE_LUMEN_COLOR)
                }
            }

        const val DAMAGE_UMBRA_COLOR = 0x5e5e5e
        val DAMAGE_UMBRA =
            object : DamageType(Main.modId - "damage_umbra") {
                init {
                    color = TextColor.fromRgb(DAMAGE_UMBRA_COLOR)
                }
            }

        const val DAMAGE_NONE_COLOR = 0xa0a0a0
        val DAMAGE_NONE =
            object : DamageType(Main.modId - "damage_none") {
                init {
                    color = TextColor.fromRgb(DAMAGE_NONE_COLOR)
                }
            }

        const val DAMAGE_BLEED_COLOR = 0xff7474
        val DAMAGE_BLEED =
            object : DamageType(Main.modId - "damage_bleed") {
                init {
                    color = TextColor.fromRgb(DAMAGE_BLEED_COLOR)
                }
            }

        val DAMAGE_THUNDER =
            object : DamageType(Main.modId - "damage_thunder") {}

        val types = ObjectUtil.getFieldsOfObject<DamageType, DamageTypes>()
    }

    object DefenseTypes {
        val DEFENSE_TRUE =
            object : DefenseType.DefenseTypeTemplate(Main.modId - "defense_true", DamageTypes.DAMAGE_TRUE) {
                override fun act(damage: Stat, defense: Stat) =
                    StatUtil.calculateTrueDamage(damage, defense)
            }

        val DEFENSE_PHYSICAL =
            object : DefenseType.DefenseTypeTemplate(Main.modId - "defense_physical", DamageTypes.DAMAGE_PHYSICAL) {}

        val DEFENSE_TERRA =
            object : DefenseType.DefenseTypeTemplate(Main.modId - "defense_terra", DamageTypes.DAMAGE_TERRA) {}

        val DEFENSE_TEMPUS =
            object : DefenseType.DefenseTypeTemplate(Main.modId - "defense_tempus", DamageTypes.DAMAGE_TEMPUS) {}

        val DEFENSE_IGNIS =
            object : DefenseType.DefenseTypeTemplate(Main.modId - "defense_ignis", DamageTypes.DAMAGE_IGNIS) {}

        val DEFENSE_AQUA =
            object : DefenseType.DefenseTypeTemplate(Main.modId - "defense_aqua", DamageTypes.DAMAGE_AQUA) {}

        val DEFENSE_LUMEN =
            object : DefenseType.DefenseTypeTemplate(Main.modId - "defense_lumen", DamageTypes.DAMAGE_LUMEN) {}

        val DEFENSE_UMBRA =
            object : DefenseType.DefenseTypeTemplate(Main.modId - "defense_umbra", DamageTypes.DAMAGE_UMBRA) {}

        val types = ObjectUtil.getFieldsOfObject<DefenseType, DefenseTypes>()
    }

    object ModifierTypes {
        val MODIFIER_ARTHROPOD =
            object : ModifierType.ModifierTypeTemplate(Main.modId - "modifier_arthropod") {
                override fun condition(event: StatEvent.OnDamageCallback.OnDamageEvent) =
                    event.damagee?.group == EntityGroup.ARTHROPOD
            }

        val MODIFIER_UNDEAD =
            object : ModifierType.ModifierTypeTemplate(Main.modId - "modifier_undead") {
                override fun condition(event: StatEvent.OnDamageCallback.OnDamageEvent) =
                    event.damagee?.group == EntityGroup.UNDEAD
            }

        val MODIFIER_UNDERWATER =
            object : ModifierType.ModifierTypeTemplate(Main.modId - "modifier_underwater") {
                override fun condition(event: StatEvent.OnDamageCallback.OnDamageEvent) =
                    event.damagee?.group == EntityGroup.AQUATIC
            }

        val MODIFIER_PLAYER =
            object : ModifierType.ModifierTypeTemplate(Main.modId - "modifier_player") {
                override fun condition(event: StatEvent.OnDamageCallback.OnDamageEvent) =
                    event.damagee is PlayerEntity
            }

        val MODIFIER_OVERALL =
            object : ModifierType(Main.modId - "modifier_overall") {
                override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
                    val damagee = event.damagee

                    if (damagee is LivingEntity) {
                        DamageManager[damagee].forEach { (type, other) ->
                            if (type is DamageType) {
                                DamageManager.replaceDamage(damagee, type, other.modifier(1.0 - event.stat.result()))
                            }
                        }
                    }
                    return ActionResult.PASS
                }
            }


        val types = ObjectUtil.getFieldsOfObject<ModifierType, ModifierTypes>()
    }

    object StatusTypes {
        val STATUS_BLEEDING = object : StatusType(Main.modId - "status_bleeding", { event ->
            StatusEffectInstance(
                Bleeding.BLEEDING,
                80,
                event.stat.result().toInt(),
                true,
                false
            )
        }) {
        }

        val types = ObjectUtil.getFieldsOfObject<StatusType, StatusTypes>()
    }

    object PowerTypes {
        class PowerCritical : PowerType(Main.modId - "power_critical") {
            fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
                val damagee = event.damagee

                if (damagee is LivingEntity) {
                    val p = event.stat.result()

                    DamageManager[damagee].forEach { (type, other) ->
                        if (type is DamageType) {
                            DamageManager.queueDamage(damagee, type, other.modifier(p))
                        }
                    }

                    val pos = damagee.pos.add(0.0, 1.0, 0.0)
                    val dPos = Vec3d.ZERO
                    val count = (15 * p).toInt()

                    damagee.spawnParticles(ParticleTypes.CRIT, pos, count, dPos, 0.5)
                }

                return ActionResult.PASS
            }
        }

        val POWER_CRITICAL = PowerCritical()

        class PowerDrain : PowerType(Main.modId - "power_drain") {
            fun afterDamage(event: StatEvent.AfterDamageCallback.AfterDamageEvent): ActionResult {
                val damager = event.damager
                val damagee = event.damagee

                val mostRecentDamage = damagee?.damageTracker?.mostRecentDamage ?: return ActionResult.PASS
                if (damager != mostRecentDamage.damageSource.source) return ActionResult.PASS

                val maxHealth = damager.maxHealth.toDouble()
                val healAmount = mostRecentDamage.damage * event.stat.result()

                min(maxHealth, (damager.health + healAmount))

                return ActionResult.PASS
            }
        }

        val POWER_DRAIN = PowerDrain()


        //val POWER_SLOWNESS =
        //    object : PowerType("power_slowness") {
        //        override fun onDamage(power: Double, event: BookStatEvent) {
        //            val entity = event.StatEvent.entity as? LivingEntity ?: return
        //            val ticks = (power * 200).toLong()
        //            EffectManager.instance.apply(
        //                EffectType.Preset.SLOWNESS,
        //                entity,
        //                power,
        //                ticks
        //            )
        //        }
        //    }
        val types = ObjectUtil.getFieldsOfObject<PowerType, PowerTypes>()
    }

    object ChanceTypes {
        val CHANCE_CRITICAL: ChanceType =
            object : ChanceType(Main.modId - "chance_critical"), StatEvent.OnDamageCallback {
                override val onDamagePriority = 1000

                override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
                    val power = event.stats.getStat(PowerTypes.POWER_CRITICAL) ?: return ActionResult.PASS
                    val p = calculate(event.stat, power)

                    return PowerTypes.POWER_CRITICAL.onDamage(event.copy(stat = p))
                }
            }

        val CHANCE_DRAIN: ChanceType =
            object : ChanceType(Main.modId - "chance_drain"), StatEvent.AfterDamageCallback {
                override val afterDamagePriority = 1000

                override fun afterDamage(event: StatEvent.AfterDamageCallback.AfterDamageEvent): ActionResult {
                    val power = event.stats.getStat(PowerTypes.POWER_DRAIN) ?: return ActionResult.PASS
                    val p = calculate(event.stat, power)

                    return PowerTypes.POWER_DRAIN.afterDamage(event.copy(stat = p))
                }
            }

        //val CHANCE_SLOWNESS =
        //    object : ChanceType.ChanceTypeTemplate("chance_slowness", PowerTypes.POWER_SLOWNESS) {}

        val types = ObjectUtil.getFieldsOfObject<ChanceType, ChanceTypes>()
    }

    object MiscTypes {
        class AttackDelay : StatType(Main.modId - "attack_delay") {
            private val map: MutableMap<UUID, Pair<Long, Int>> = mutableMapOf()

            fun setDelay(damager: PlayerEntity, delay: Int) {
                if (canDamage(damager)) {
                    val time = damager.world.time

                    map[damager.uuid] = time to delay
                }
            }

            fun canDamage(damager: PlayerEntity): Boolean {
                return map[damager.uuid]?.let { (time, delay) ->
                    val d = abs(damager.world.time - time)
                    //println("delay: $delay, delta: $d")

                    d >= delay
                } ?: true
            }
        }

        val ATTACK_DELAY = AttackDelay()


        val types = ObjectUtil.getFieldsOfObject<StatType, MiscTypes>()
    }

    val types = DamageTypes.types +
            DefenseTypes.types +
            ModifierTypes.types +
            StatusTypes.types +
            PowerTypes.types +
            ChanceTypes.types +
            MiscTypes.types
}