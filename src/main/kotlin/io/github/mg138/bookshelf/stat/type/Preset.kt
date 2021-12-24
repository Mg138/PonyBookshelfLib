package io.github.mg138.bookshelf.stat.type

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.event.AfterDamageListener
import io.github.mg138.bookshelf.stat.type.event.OnDamageListener
import io.github.mg138.bookshelf.stat.type.template.*
import io.github.mg138.bookshelf.stat.utils.StatUtil
import io.github.mg138.bookshelf.utils.ObjectUtil
import io.github.mg138.bookshelf.utils.minus
import net.minecraft.entity.EntityGroup
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.TextColor
import net.minecraft.util.ActionResult
import java.lang.Double.min

@Suppress("UNUSED")
object Preset {
    object DamageTypes {
        val DAMAGE_TRUE =
            object : DamageType(Main.modId - "damage_true") {}

        val DAMAGE_PHYSICAL =
            object : DamageType(Main.modId - "damage_physical") {
                init {
                    numberColor = TextColor.parse("#f5ab3d")!!
                }
            }

        val DAMAGE_TERRA =
            object : DamageType(Main.modId - "damage_terra") {
                init {
                    numberColor = TextColor.parse("#7bb651")!!
                }
            }

        val DAMAGE_TEMPUS =
            object : DamageType(Main.modId - "damage_tempus") {
                init {
                    numberColor = TextColor.parse("#ffe494")!!
                }
            }

        val DAMAGE_IGNIS =
            object : DamageType(Main.modId - "damage_ignis") {
                init {
                    numberColor = TextColor.parse("#f8822f")!!
                }
            }

        val DAMAGE_AQUA =
            object : DamageType(Main.modId - "damage_aqua") {
                init {
                    numberColor = TextColor.parse("#7291ff")!!
                }
            }

        val DAMAGE_LUMEN =
            object : DamageType(Main.modId - "damage_lumen") {
                init {
                    numberColor = TextColor.parse("#fff46e")!!
                }
            }

        val DAMAGE_UMBRA =
            object : DamageType(Main.modId - "damage_umbra") {
                init {
                    numberColor = TextColor.parse("#5e5e5e")!!
                }
            }

        val DAMAGE_NONE =
            object : DamageType(Main.modId - "damage_none") {
                init {
                    numberColor = TextColor.parse("#a0a0a0")!!
                }
            }

        val DAMAGE_BLEED =
            object : DamageType(Main.modId - "damage_bleed") {
                init {
                    numberColor = TextColor.parse("#ff7474")!!
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
            object :
                DefenseType.DefenseTypeTemplate(Main.modId - "defense_physical", DamageTypes.DAMAGE_PHYSICAL) {}

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
                override fun condition(event: OnDamageListener.OnDamageEvent) =
                    event.damagee.group == EntityGroup.ARTHROPOD
            }

        val MODIFIER_UNDEAD =
            object : ModifierType.ModifierTypeTemplate(Main.modId - "modifier_undead") {
                override fun condition(event: OnDamageListener.OnDamageEvent) =
                    event.damagee.group == EntityGroup.UNDEAD
            }

        val MODIFIER_UNDERWATER =
            object : ModifierType.ModifierTypeTemplate(Main.modId - "modifier_underwater") {
                override fun condition(event: OnDamageListener.OnDamageEvent) =
                    event.damagee.group == EntityGroup.AQUATIC
            }

        val MODIFIER_PLAYER =
            object : ModifierType.ModifierTypeTemplate(Main.modId - "modifier_player") {
                override fun condition(event: OnDamageListener.OnDamageEvent) =
                    event.damagee is PlayerEntity
            }


        val types = ObjectUtil.getFieldsOfObject<ModifierType, ModifierTypes>()
    }

    object PowerTypes {
        class PowerCritical : PowerType(Main.modId - "power_critical") {
            fun onDamage(event: OnDamageListener.OnDamageEvent): ActionResult {
                val damagee = event.damagee

                DamageManager[damagee]?.forEach { (type, other) ->
                    if (type is DamageType) {
                        DamageManager.queueDamage(damagee, type, other * event.stat.result())
                    }
                }
                return ActionResult.PASS
            }
        }
        val POWER_CRITICAL = PowerCritical()

        class PowerDrain : PowerType(Main.modId - "power_drain") {
            fun afterDamage(event: AfterDamageListener.AfterDamageEvent): ActionResult {
                val damager = event.damager
                val damagee = event.damagee

                val maxHealth = damager.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH)
                val mostRecentDamage = damagee.damageTracker.mostRecentDamage ?: return ActionResult.PASS
                if (damager != mostRecentDamage.damageSource.source) return ActionResult.PASS

                val healAmount = mostRecentDamage.damage

                min(maxHealth, (damager.health + healAmount).toDouble())

                return ActionResult.PASS
            }
        }
        val POWER_DRAIN = PowerDrain()


        //val POWER_SLOWNESS =
        //    object : PowerType("power_slowness") {
        //        override fun onDamage(power: Double, event: BookDamageEvent) {
        //            val entity = event.damageEvent.entity as? LivingEntity ?: return
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
            object : ChanceType(Main.modId - "chance_critical"), OnDamageListener {
                override val onDamagePriority = 1000

                override fun onDamage(event: OnDamageListener.OnDamageEvent): ActionResult {
                    val power = event.item.getStat(PowerTypes.POWER_CRITICAL) ?: return ActionResult.PASS
                    val p = calculate(event.stat, power)

                    return PowerTypes.POWER_CRITICAL.onDamage(event.copy(stat=p))
                }
            }

        val CHANCE_DRAIN: ChanceType =
            object : ChanceType(Main.modId - "chance_drain"), AfterDamageListener {
                override val afterDamagePriority = 1000

                override fun afterDamage(event: AfterDamageListener.AfterDamageEvent): ActionResult {
                    val power = event.item.getStat(PowerTypes.POWER_DRAIN) ?: return ActionResult.PASS
                    val p = calculate(event.stat, power)

                    return PowerTypes.POWER_DRAIN.afterDamage(event.copy(stat=p))
                }
            }

        //val CHANCE_SLOWNESS =
        //    object : ChanceType.ChanceTypeTemplate("chance_slowness", PowerTypes.POWER_SLOWNESS) {}

        val types = ObjectUtil.getFieldsOfObject<ChanceType, ChanceTypes>()
    }

    val types = DamageTypes.types +
            DefenseTypes.types +
            ModifierTypes.types +
            PowerTypes.types +
            ChanceTypes.types
}