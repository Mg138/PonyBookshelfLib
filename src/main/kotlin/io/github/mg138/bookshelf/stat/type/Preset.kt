package io.github.mg138.bookshelf.stat.type

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.utils.minus
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.template.DamageType
import io.github.mg138.bookshelf.stat.type.template.DefenseType
import io.github.mg138.bookshelf.utils.ObjectUtil
import net.minecraft.text.TextColor

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
                override fun damageCalc(damage: Stat, stat: Stat): Stat {
                    return damage - stat.result()
                }
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

    //object ModifierTypes {
    //    val MODIFIER_ARTHROPOD =
    //        object : ModifierType.ModifierTypeTemplate("MODIFIER_ARTHROPOD") {
    //            override fun condition(it: Double, event: BookDamageEvent) =
    //                MobTypeUtil.isArthropod(event.damageEvent.entity.type)
    //        }
//
    //    val MODIFIER_UNDEAD =
    //        object : ModifierType.ModifierTypeTemplate("MODIFIER_UNDEAD") {
    //            override fun condition(it: Double, event: BookDamageEvent) =
    //                MobTypeUtil.isUndead(event.damageEvent.entity.type)
    //        }
//
    //    val MODIFIER_MOBS =
    //        object : ModifierType.ModifierTypeTemplate("MODIFIER_MOBS") {
    //            override fun condition(it: Double, event: BookDamageEvent) =
    //                MobTypeUtil.isMob(event.damageEvent.entity.type)
    //        }
//
    //    val MODIFIER_HELL =
    //        object : ModifierType.ModifierTypeTemplate("MODIFIER_HELL") {
    //            override fun condition(it: Double, event: BookDamageEvent) =
    //                MobTypeUtil.isHellish(event.damageEvent.entity.type)
    //        }
//
    //    val MODIFIER_UNDERWATER =
    //        object : ModifierType.ModifierTypeTemplate("MODIFIER_UNDERWATER") {
    //            override fun condition(it: Double, event: BookDamageEvent) =
    //                MobTypeUtil.isWatery(event.damageEvent.entity.type)
    //        }
//
    //    val MODIFIER_PLAYER =
    //        object : ModifierType.ModifierTypeTemplate("MODIFIER_PLAYER") {
    //            override fun condition(it: Double, event: BookDamageEvent) =
    //                event.damageEvent.entity is Player
    //        }
//
//
    //    val types = PresetUtil.getObjectPropertiesOfType<ModifierType, ModifierTypes>()
    //}
//
    //object PowerTypes {
    //    val POWER_CRITICAL =
    //        object : PowerType("POWER_CRITICAL") {
    //            override fun onDamage(power: Double, event: BookDamageEvent) {
    //                event.damagerStat.map { (type, stat) ->
    //                    if (type is DamageType) {
    //                        stat * power
    //                    }
    //                }
    //            }
    //        }
//
    //    val POWER_DRAIN =
    //        object : PowerType("POWER_DRAIN") {
    //            override fun onDamage(power: Double, event: BookDamageEvent) {
    //                val damageEvent = event.damageEvent as? EntityDamageByEntityEvent ?: return
    //                val damager = damageEvent.damager as? LivingEntity ?: return
    //                val maxHealth = damager.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue ?: return
//
    //                val healAmount = damageEvent.damage * power
    //                damager.health = min(damager.health + healAmount, maxHealth)
    //            }
    //        }
//
    //    val POWER_SLOWNESS =
    //        object : PowerType("POWER_SLOWNESS") {
    //            override fun onDamage(power: Double, event: BookDamageEvent) {
    //                val entity = event.damageEvent.entity as? LivingEntity ?: return
//
    //                val ticks = (power * 200).toLong()
//
    //                EffectManager.instance.apply(
    //                    EffectType.Preset.SLOWNESS,
    //                    entity,
    //                    power,
    //                    ticks
    //                )
    //            }
//
    //        }
//
    //    val POWER_NAUSEOUS =
    //        object : PowerType("POWER_NAUSEOUS") {
    //            override fun onDamage(power: Double, event: BookDamageEvent) {
    //                val entity = event.damageEvent.entity as? LivingEntity ?: return
//
    //                val ticks = (20 * power).toLong()
//
    //                if (ticks >= 20) {
    //                    EffectManager.instance.apply(
    //                        EffectType.Preset.NAUSEOUS,
    //                        entity,
    //                        0.0,
    //                        ticks
    //                    )
    //                }
    //            }
    //        }
//
    //    val POWER_LEVITATION =
    //        object : PowerType("POWER_LEVITATION") {
    //            override fun onDamage(power: Double, event: BookDamageEvent) {
    //                val entity = event.damageEvent.entity as? LivingEntity ?: return
//
    //                val ticks = (20 * power).toLong()
//
    //                if (ticks >= 20) {
    //                    EffectManager.instance.apply(
    //                        EffectType.Preset.LEVITATION,
    //                        entity,
    //                        0.0,
    //                        ticks
    //                    )
    //                }
    //            }
//
    //        }
//
    //    val types = PresetUtil.getObjectPropertiesOfType<PowerType, PowerTypes>()
    //}
//
    //object ChanceTypes {
    //    val CHANCE_CRITICAL =
    //        object : ChanceType.ChanceTypeTemplate("CHANCE_CRITICAL", PowerTypes.POWER_CRITICAL) {}
//
    //    val CHANCE_DRAIN =
    //        object : ChanceType.ChanceTypeTemplate("CHANCE_DRAIN", PowerTypes.POWER_DRAIN) {}
//
    //    val CHANCE_SLOWNESS =
    //        object : ChanceType.ChanceTypeTemplate("CHANCE_SLOWNESS", PowerTypes.POWER_SLOWNESS) {}
//
    //    val CHANCE_NAUSEOUS =
    //        object : ChanceType.ChanceTypeTemplate("CHANCE_NAUSEOUS", PowerTypes.POWER_NAUSEOUS) {}
//
    //    val CHANCE_LEVITATION =
    //        object : ChanceType.ChanceTypeTemplate("CHANCE_LEVITATION", PowerTypes.POWER_LEVITATION) {}
//
    //    val types = PresetUtil.getObjectPropertiesOfType<ChanceType, ChanceTypes>()
    //}

    val types = listOf(
        *DamageTypes.types.toTypedArray(),
        *DefenseTypes.types.toTypedArray(),
        //*ModifierTypes.types.toTypedArray(),
        //*PowerTypes.types.toTypedArray(),
        //*ChanceTypes.types.toTypedArray()
    )
}