package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.LoredStatType
import io.github.mg138.bookshelf.utils.StatUtil
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

abstract class DefenseType(id: Identifier) :
    LoredStatType(id), StatEvent.OnDamageCallback, StatEvent.DefensiveStat {
    override val onDamagePriority = 10000000

    abstract class DefenseTypeTemplate(
        id: Identifier,
        private val damageType: DamageType
    ) : DefenseType(id) {
        override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
            val damagee = event.damagee

            DamageManager[damagee].map
                .map { it.value }
                .forEach { stats ->
                    stats.computeIfPresent(damageType) { _, damage ->
                        act(damage, event.stat)
                    }
                }
            return ActionResult.PASS
        }

        open fun act(damage: Stat, defense: Stat) = StatUtil.defense(damage, defense)
    }
}