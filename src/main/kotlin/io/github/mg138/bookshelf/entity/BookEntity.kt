package io.github.mg138.bookshelf.entity

import eu.pb4.polymer.api.entity.PolymerEntity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.world.World

abstract class BookEntity<T: BookEntity<T>>(
    type: EntityType<T>, world: World
) : PolymerEntity, LivingEntity(type, world)