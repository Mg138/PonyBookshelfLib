package io.github.mg138.bookshelf.entity

import eu.pb4.polymer.api.entity.PolymerEntity
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.mob.MobEntity.createMobAttributes

fun bookAttributes() =
    createMobAttributes()
        .add(EntityAttributes.GENERIC_MAX_HEALTH, 1024.0)!!

interface BookEntity : PolymerEntity