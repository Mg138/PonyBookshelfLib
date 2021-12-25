package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

abstract class DamageType(id: Identifier) :
    StatType(id), StatEvent.OnDamageCallback {
    override val onDamagePriority = 0

    override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
        DamageManager.queueDamage(event.damagee, this, event.stat)

        return ActionResult.PASS
    }
}