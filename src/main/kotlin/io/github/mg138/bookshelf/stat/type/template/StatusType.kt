package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.stat.event.StatEvent
import io.github.mg138.bookshelf.stat.type.LoredStatType
import net.minecraft.util.ActionResult
import net.minecraft.util.Identifier

abstract class StatusType(
    id: Identifier,
    private val effect: (StatEvent.OnDamageCallback.OnDamageEvent) -> Unit
) : LoredStatType(id), StatEvent.OnDamageCallback {
    override val onDamagePriority = 10

    override fun onDamage(event: StatEvent.OnDamageCallback.OnDamageEvent): ActionResult {
        effect(event)

        return ActionResult.PASS
    }
}