package io.github.mg138.bookshelf.effect.impl

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.effect.BookStatusEffectInfo
import io.github.mg138.bookshelf.effect.StatusEffectManager
import io.github.mg138.bookshelf.effect.template.FlagEffect
import io.github.mg138.bookshelf.entity.StatedEntity
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.StatTypes
import io.github.mg138.bookshelf.utils.minus

object FallResistance : FlagEffect() {
    override fun start(effect: BookStatusEffectInfo) {
        val entity = effect.entity
        if (entity is StatedEntity) {
            entity.getStats().addStat(StatTypes.MiscTypes.FallResistance, StatSingle(effect.power))
        }
    }

    override fun end(effect: BookStatusEffectInfo) {
        val entity = effect.entity
        if (entity is StatedEntity) {
            entity.getStats().addStat(StatTypes.MiscTypes.FallResistance, StatSingle(-effect.power))
        }
    }

    fun register() {
        StatusEffectManager.register(Main.modId - "fall_resistance", this)
    }
}