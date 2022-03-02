package io.github.mg138.bookshelf.effect

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.entity.LivingEntity
import net.minecraft.world.World

object ActiveEffectManager {
    private val map: MutableMap<LivingEntity, MutableMap<BookStatusEffect, BookStatusEffectInfo>> = mutableMapOf()

    fun removeEffect(entity: LivingEntity, effect: BookStatusEffect) {
        if (entity.isDead) {
            map[entity]?.forEach { (effect, info) ->
                effect.end(info)
            }
        } else {
            map[entity]?.remove(effect)?.run {
                effect.end(this)
            }
        }
    }

    fun addEffect(info: BookStatusEffectInfo, effect: BookStatusEffect) {
        val entity = info.entity
        val effects = map.getOrPut(entity) { mutableMapOf() }

        val new = effects[effect]?.let { old ->
            effect.merge(old, info)
        } ?: info

        removeEffect(entity, effect)
        effects[effect] = new
        effect.start(new)
    }

    fun addEffect(entity: LivingEntity, duration: Int, power: Double, effect: BookStatusEffect) {
        return addEffect(BookStatusEffectInfo(entity, duration, power, entity.world.time), effect)
    }

    private fun actEffects(world: World) {
        val currentTime = world.time

        map.forEach { (entity, map) ->
            val removed: MutableList<BookStatusEffect> = mutableListOf()

            map.forEach { (effect, info) ->
                val dT = currentTime - info.startTime

                if (dT > info.duration || info.entity.isDead) {
                    removed += effect
                } else {
                    if (dT > 0) {
                        effect.act(info, currentTime)
                    }
                }
            }
            removed.forEach {
                removeEffect(entity, it)
            }
        }
    }

    fun register() {
        ServerTickEvents.END_WORLD_TICK.register(::actEffects)
    }
}