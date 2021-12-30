package io.github.mg138.bookshelf.entity

import io.github.mg138.bookshelf.stat.data.StatMap
import net.minecraft.entity.EntityType
import net.minecraft.world.World

abstract class BookStaticStatedEntity<T : BookStaticStatedEntity<T>>(
    type: EntityType<T>, world: World,
    private val statMap: StatMap
) : BookStatedEntity<T>(type, world) {
    override fun getStatMap() = statMap
}