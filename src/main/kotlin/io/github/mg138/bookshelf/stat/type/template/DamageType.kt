package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.type.event.OnDamageListener
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

abstract class DamageType(id: Identifier) :
    StatType(id), OnDamageListener {
    override val onDamagePriority = 0

    override fun onDamage(event: OnDamageListener.OnDamageEvent): ActionResult {
        DamageManager.queueDamage(event.damagee, this, event.stat)

        return ActionResult.PASS
    }
}