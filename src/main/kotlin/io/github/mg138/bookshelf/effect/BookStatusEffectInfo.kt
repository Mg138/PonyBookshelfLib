package io.github.mg138.bookshelf.effect

import net.minecraft.entity.LivingEntity

data class BookStatusEffectInfo(val entity: LivingEntity, val duration: Int, val power: Double, val startTime: Long)
