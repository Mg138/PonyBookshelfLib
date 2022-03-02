package io.github.mg138.bookshelf.stat.type

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.effect.ActiveEffectManager
import io.github.mg138.bookshelf.effect.impl.Bleeding
import io.github.mg138.bookshelf.entity.StatedEntity
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.template.*
import io.github.mg138.bookshelf.utils.ParticleUtil.spawnParticles
import io.github.mg138.bookshelf.utils.StatUtil
import io.github.mg138.bookshelf.utils.minus
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.particle.ParticleTypes
import net.minecraft.text.TextColor
import net.minecraft.util.ActionResult
import net.minecraft.util.math.Vec3d
import java.util.*
import kotlin.math.abs
import kotlin.math.min

@Suppress("UNUSED")
object StatTypes {
    object DamageTypes {
        object DamageTrue : DamageType(Main.modId - "damage_true")

        object DamagePhysical : DamageType(Main.modId - "damage_physical") {
            const val DAMAGE_PHYSICAL_COLOR = 0xf5ab3d

            init {
                color = TextColor.fromRgb(DAMAGE_PHYSICAL_COLOR)
            }
        }

        object DamageTerra : DamageType(Main.modId - "damage_terra") {
            const val DAMAGE_TERRA_COLOR = 0x7bb651

            init {
                color = TextColor.fromRgb(DAMAGE_TERRA_COLOR)
            }
        }

        object DamageTempus : DamageType(Main.modId - "damage_tempus") {
            const val DAMAGE_TEMPUS_COLOR = 0xffe494

            init {
                color = TextColor.fromRgb(DAMAGE_TEMPUS_COLOR)
            }
        }

        object DamageIgnis : DamageType(Main.modId - "damage_ignis") {
            const val DAMAGE_IGNIS_COLOR = 0xf8822f

            init {
                color = TextColor.fromRgb(DAMAGE_IGNIS_COLOR)
            }
        }

        object DamageAqua : DamageType(Main.modId - "damage_aqua") {
            const val DAMAGE_AQUA_COLOR = 0x7291ff

            init {
                color = TextColor.fromRgb(DAMAGE_AQUA_COLOR)
            }
        }

        object DamageLumen : DamageType(Main.modId - "damage_lumen") {
            const val DAMAGE_LUMEN_COLOR = 0xfff46e

            init {
                color = TextColor.fromRgb(DAMAGE_LUMEN_COLOR)
            }
        }

        object DamageUmbra : DamageType(Main.modId - "damage_umbra") {
            const val DAMAGE_UMBRA_COLOR = 0x5e5e5e

            init {
                color = TextColor.fromRgb(DAMAGE_UMBRA_COLOR)
            }
        }

        object DamageNone : DamageType(Main.modId - "damage_none") {
            const val DAMAGE_NONE_COLOR = 0xa0a0a0

            init {
                color = TextColor.fromRgb(DAMAGE_NONE_COLOR)
            }
        }

        object DamageBleed : DamageType(Main.modId - "damage_bleed") {
            const val DAMAGE_BLEED_COLOR = 0xff7474

            init {
                color = TextColor.fromRgb(DAMAGE_BLEED_COLOR)
            }
        }

        object DamageThunder : DamageType(Main.modId - "damage_thunder")

        val types: List<StatType> = listOf(
            DamageTrue,
            DamagePhysical,
            DamageTerra,
            DamageTempus,
            DamageIgnis,
            DamageAqua,
            DamageLumen,
            DamageUmbra,
            DamageNone,
            DamageBleed,
            DamageThunder
        )
    }

    object DefenseTypes {
        object DefenseTrue : DefenseType.DefenseTypeTemplate(Main.modId - "defense_true", DamageTypes.DamageTrue) {
            override fun act(damage: Stat, defense: Stat) =
                StatUtil.calculateTrueDamage(damage, defense)
        }

        object DefensePhysical :
            DefenseType.DefenseTypeTemplate(Main.modId - "defense_physical", DamageTypes.DamagePhysical)

        object DefenseTerra :
            DefenseType.DefenseTypeTemplate(Main.modId - "defense_terra", DamageTypes.DamageTerra)

        object DefenseTempus :
            DefenseType.DefenseTypeTemplate(Main.modId - "defense_tempus", DamageTypes.DamageTempus)

        object DefenseIgnis :
            DefenseType.DefenseTypeTemplate(Main.modId - "defense_ignis", DamageTypes.DamageIgnis)

        object DefenseAqua :
            DefenseType.DefenseTypeTemplate(Main.modId - "defense_aqua", DamageTypes.DamageAqua)

        object DefenseLumen :
            DefenseType.DefenseTypeTemplate(Main.modId - "defense_lumen", DamageTypes.DamageLumen)

        object DefenseUmbra :
            DefenseType.DefenseTypeTemplate(Main.modId - "defense_umbra", DamageTypes.DamageUmbra)

        val types: List<StatType> = listOf(
            DefenseTrue,
            DefensePhysical,
            DefenseTerra,
            DefenseTempus,
            DefenseIgnis,
            DefenseAqua,
            DefenseLumen,
            DefenseUmbra
        )
    }

    object ModifierTypes {
        object ModifierOverall : ModifierType.StatTypeModifierTemplate(Main.modId - "modifier_overall") {
            override fun condition(type: StatType) = type is DamageType
        }

        object ModifierPhysical : ModifierType.StatTypeModifierTemplate(Main.modId - "modifier_physical") {
            override fun condition(type: StatType) = type is DamageTypes.DamagePhysical
        }

        object ModifierTerra : ModifierType.StatTypeModifierTemplate(Main.modId - "modifier_terra") {
            override fun condition(type: StatType) = type is DamageTypes.DamageTerra
        }

        object ModifierTempus : ModifierType.StatTypeModifierTemplate(Main.modId - "modifier_tempus") {
            override fun condition(type: StatType) = type is DamageTypes.DamageTempus
        }

        object ModifierIgnis : ModifierType.StatTypeModifierTemplate(Main.modId - "modifier_ignis") {
            override fun condition(type: StatType) = type is DamageTypes.DamageIgnis
        }

        object ModifierAqua : ModifierType.StatTypeModifierTemplate(Main.modId - "modifier_aqua") {
            override fun condition(type: StatType) = type is DamageTypes.DamageAqua
        }

        object ModifierLumen : ModifierType.StatTypeModifierTemplate(Main.modId - "modifier_lumen") {
            override fun condition(type: StatType) = type is DamageTypes.DamageLumen
        }

        object ModifierUmbra : ModifierType.StatTypeModifierTemplate(Main.modId - "modifier_umbra") {
            override fun condition(type: StatType) = type is DamageTypes.DamageUmbra
        }


        val types: List<StatType> = listOf(
            ModifierOverall,
            ModifierPhysical,
            ModifierTerra,
            ModifierTempus,
            ModifierIgnis,
            ModifierAqua,
            ModifierLumen,
            ModifierUmbra
        )
    }


    object StatusTypes {
        object StatusBleeding : StatusType(Main.modId - "status_bleeding", { event ->
            event.damagee.let {
                ActiveEffectManager.addEffect(
                    entity = it,
                    duration = 80,
                    power = event.stat.result(),
                    effect = Bleeding
                )
            }
        })

        val types: List<StatType> = listOf(StatusBleeding)
    }

    object PowerTypes {
        object PowerCritical : PowerType(Main.modId - "power_critical") {
            fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
                val damagee = event.damagee

                val p = event.stat.result()

                DamageManager[damagee].map.flatMap { it.value }.forEach { (type, other) ->
                    if (type is DamageType) {
                        DamageManager.replaceDamageOfAllSource(
                            damagee,
                            type,
                            StatUtil.positiveModifier(other, 1 + p)
                        )
                    }
                }

                val pos = damagee.pos.add(0.0, 1.0, 0.0)
                val dPos = Vec3d.ZERO
                val count = (15 * p).toInt()

                damagee.spawnParticles(ParticleTypes.CRIT, pos, count, dPos, 0.5)

                return ActionResult.PASS
            }
        }

        object PowerDrain : PowerType(Main.modId - "power_drain") {
            fun afterDamage(event: StatEvent.AfterDamageCallback.AfterDamageEvent): ActionResult {
                val source = event.source
                val damager = (source?.source as? LivingEntity) ?: return ActionResult.PASS
                val damagee = event.damagee

                val mostRecentDamage = damagee.damageTracker?.mostRecentDamage ?: return ActionResult.PASS
                if (damager != mostRecentDamage.damageSource.source) return ActionResult.PASS

                val maxHealth = damager.maxHealth
                val healAmount = (mostRecentDamage.damage * event.stat.result()).toFloat()

                damager.health = min(maxHealth, (damager.health + healAmount))

                return ActionResult.PASS
            }
        }

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

        val types: List<StatType> = listOf(
            PowerCritical,
            PowerDrain
        )
    }

    object ChanceTypes {
        // TODO template

        object ChanceCritical : ChanceType(Main.modId - "chance_critical"),
            StatEvent.OnDamageCallback,
            StatEvent.OffensiveStat {
            override val onDamagePriority = 1000

            override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
                val power = event.damageeStats?.getStat(PowerTypes.PowerCritical) ?: return ActionResult.PASS
                val p = calculate(event.stat, power)

                return PowerTypes.PowerCritical.onDamage(event.copy(stat = p))
            }
        }

        object ChanceDrain : ChanceType(Main.modId - "chance_drain"),
            StatEvent.AfterDamageCallback,
            StatEvent.OffensiveStat {
            override val afterDamagePriority = 1000

            override fun afterDamage(event: StatEvent.AfterDamageCallback.AfterDamageEvent): ActionResult {
                val power = event.damageeStats?.getStat(PowerTypes.PowerDrain) ?: return ActionResult.PASS
                val p = calculate(event.stat, power)

                return PowerTypes.PowerDrain.afterDamage(event.copy(stat = p))
            }
        }

        //val CHANCE_SLOWNESS =
        //    object : ChanceType.ChanceTypeTemplate("chance_slowness", PowerTypes.POWER_SLOWNESS) {}

        val types: List<StatType> = listOf(ChanceCritical, ChanceDrain)
    }

    object MiscTypes {
        object Health : StatType(Main.modId - "health")

        object MaxHealth : StatType(Main.modId - "max_health"), StatEvent.AfterDamageCallback, StatEvent.DefensiveStat {
            override fun afterDamage(event: StatEvent.AfterDamageCallback.AfterDamageEvent): ActionResult {
                val damagee = event.damagee
                if (damagee is StatedEntity) {
                    val stats = damagee.getStats()
                    val health = stats[Health] ?: return ActionResult.PASS
                    val max = stats[MaxHealth] ?: return ActionResult.PASS

                    if (health > max) {
                        stats.putStat(Health, max)
                    }
                }
                return ActionResult.PASS
            }
        }

        object AttackDelay : StatType(Main.modId - "attack_delay"), StatEvent.OffensiveStat {
            private val map: MutableMap<UUID, Pair<Long, Int>> = mutableMapOf()

            fun setDelay(damager: PlayerEntity, delay: Int) {
                val time = damager.world.time

                map[damager.uuid] = time to delay
            }

            fun canDamage(damager: PlayerEntity): Boolean {
                return map[damager.uuid]?.let { (time, delay) ->
                    val d = abs(damager.world.time - time)

                    d >= delay
                } ?: true
            }
        }

        object FallResistance : StatType(Main.modId - "fall_resistance"), StatEvent.OnDamageCallback,
            StatEvent.DefensiveStat {
            override val onDamagePriority = 2000000000

            override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
                event.source?.let { source ->
                    if (source.isFromFalling) {
                        val res = event.stat
                        event.damageeStats?.forEach { (type, stat) ->
                            if (type is DamageType) {
                                DamageManager.replaceDamageOfAllSource(event.damagee, type, StatUtil.defense(stat, res))
                            }
                        }
                    }
                }
                return ActionResult.PASS
            }
        }

        val types: List<StatType> = listOf(
            Health,
            MaxHealth,
            AttackDelay,
            FallResistance
        )
    }

    val types: List<StatType> = DamageTypes.types +
            DefenseTypes.types +
            ModifierTypes.types +
            StatusTypes.types +
            PowerTypes.types +
            ChanceTypes.types +
            MiscTypes.types
}