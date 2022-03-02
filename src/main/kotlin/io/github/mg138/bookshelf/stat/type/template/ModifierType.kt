package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

abstract class ModifierType(id: Identifier) :
    PercentageStatType(id), StatEvent.OnDamageCallback, StatEvent.OffensiveStat {
    override val onDamagePriority = 1000000000

    abstract class StatTypeModifierTemplate(
        id: Identifier
    ) : ModifierType(id) {
        abstract fun condition(type: StatType): Boolean

        override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
            val damagee = event.damagee

            DamageManager[damagee]
                .map
                .flatMap { it.value }
                .filter { condition(it.first) }
                .forEach { (type, other) ->
                    DamageManager.queueDamage(
                        damagee,
                        type,
                        other.modifier(event.stat.result()),
                        event.source
                    )
                }
            return ActionResult.PASS
        }
    }
}