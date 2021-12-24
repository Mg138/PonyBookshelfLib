package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.type.event.OnDamageListener
import io.github.mg138.bookshelf.stat.utils.StatUtil
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

abstract class ModifierType(id: Identifier) :
    PercentageStatType(id), OnDamageListener {
    override val onDamagePriority = 1

    abstract class ModifierTypeTemplate(
        id: Identifier
    ) : ModifierType(id) {
        abstract fun condition(event: OnDamageListener.OnDamageEvent): Boolean

        override fun onDamage(event: OnDamageListener.OnDamageEvent): ActionResult {
            if (condition(event)) {
                val damagee = event.damagee
                DamageManager[damagee]?.forEach { (type, other) ->
                    if (type is DamageType) {
                        DamageManager.queueDamage(damagee, type, StatUtil.damageModifier(other, event.stat))
                    }
                }
            }
            return ActionResult.PASS
        }
    }
}