package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.type.event.OnDamageListener
import io.github.mg138.bookshelf.stat.utils.StatUtil
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

abstract class DefenseType(id: Identifier) :
    StatType(id), OnDamageListener {
    override val onDamagePriority = 10000000

    abstract class DefenseTypeTemplate(
        id: Identifier,
        private val damageType: DamageType
    ) : DefenseType(id) {
        override fun onDamage(event: OnDamageListener.OnDamageEvent): ActionResult {
            val damagee = event.damagee

            DamageManager[damagee]?.let {
                it[damageType]?.let { damage ->
                    DamageManager.queueDamage(damagee, damageType, act(damage, event.stat))
                }
            }
            return ActionResult.PASS
        }

        open fun act(damage: Stat, defense: Stat) = StatUtil.defense(damage, defense)
    }
}