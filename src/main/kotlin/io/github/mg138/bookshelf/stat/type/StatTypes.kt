package io.github.mg138.bookshelf.stat.type

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.effect.Bleeding
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.template.*
import io.github.mg138.bookshelf.utils.ParticleUtil.spawnParticles
import io.github.mg138.bookshelf.utils.StatUtil
import io.github.mg138.bookshelf.utils.minus
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

        val types = listOf(
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

        val types = listOf(
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


        val types = listOf(
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
            StatusEffectInstance(
                Bleeding.BLEEDING,
                80,
                event.stat.result().toInt(),
                true,
                false
            )
        })

        val types = listOf(StatusBleeding)
    }

    object PowerTypes {
        object PowerCritical : PowerType(Main.modId - "power_critical") {
            fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
                val damagee = event.damagee

                if (damagee is LivingEntity) {
                    val p = event.stat.result()

                    DamageManager[damagee].forEach { (type, other) ->
                        if (type is DamageType) {
                            DamageManager.replaceDamage(damagee, type, other.modifier(p))
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

        object PowerDrain : PowerType(Main.modId - "power_drain") {
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

        val types = listOf(PowerCritical, PowerDrain)
    }

    object ChanceTypes {
        object ChanceCritical : ChanceType(Main.modId - "chance_critical"), StatEvent.OnDamageCallback {
            override val onDamagePriority = 1000

            override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
                val power = event.stats.getStat(PowerTypes.PowerCritical) ?: return ActionResult.PASS
                val p = calculate(event.stat, power)

                return PowerTypes.PowerCritical.onDamage(event.copy(stat = p))
            }
        }

        object ChanceDrain : ChanceType(Main.modId - "chance_drain"), StatEvent.AfterDamageCallback {
            override val afterDamagePriority = 1000

            override fun afterDamage(event: StatEvent.AfterDamageCallback.AfterDamageEvent): ActionResult {
                val power = event.stats.getStat(PowerTypes.PowerDrain) ?: return ActionResult.PASS
                val p = calculate(event.stat, power)

                return PowerTypes.PowerDrain.afterDamage(event.copy(stat = p))
            }
        }

        //val CHANCE_SLOWNESS =
        //    object : ChanceType.ChanceTypeTemplate("chance_slowness", PowerTypes.POWER_SLOWNESS) {}

        val types = listOf(ChanceCritical, ChanceDrain)
    }

    object MiscTypes {
        object AttackDelay : StatType(Main.modId - "attack_delay") {
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

        val types = listOf(AttackDelay)
    }

    val types = DamageTypes.types +
            DefenseTypes.types +
            ModifierTypes.types +
            StatusTypes.types +
            PowerTypes.types +
            ChanceTypes.types +
            MiscTypes.types
}