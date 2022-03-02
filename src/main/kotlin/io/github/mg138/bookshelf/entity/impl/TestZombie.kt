package io.github.mg138.bookshelf.entity.impl

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.entity.BookEntity
import io.github.mg138.bookshelf.entity.StatedEntity
import io.github.mg138.bookshelf.entity.bookAttributes
import io.github.mg138.bookshelf.stat.data.StatMap
import io.github.mg138.bookshelf.stat.stat.StatRange
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.StatTypes
import io.github.mg138.bookshelf.utils.minus
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.mob.PathAwareEntity
import net.minecraft.entity.mob.ZombieEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.Arm
import net.minecraft.util.registry.Registry
import net.minecraft.world.World

class TestZombie(type: EntityType<TestZombie>, world: World) :
    PathAwareEntity(type, world), StatedEntity, BookEntity {
    companion object {
        val ZOMBIE: EntityType<TestZombie> = Registry.register(
            Registry.ENTITY_TYPE,
            Main.modId - "test_zombie",
            FabricEntityTypeBuilder
                .create(SpawnGroup.MISC, ::TestZombie)
                .dimensions(EntityType.ZOMBIE.dimensions)
                .disableSaving()
                .build()
        )

        fun register() {
            FabricDefaultAttributeRegistry.register(ZOMBIE, bookAttributes())
        }
    }

    private val statMap: StatMap = StatMap().apply {
        putStat(StatTypes.MiscTypes.MaxHealth, StatSingle(50.0))
        putStat(StatTypes.DamageTypes.DamageTerra, StatRange(5.0, 10.0))
    }

    override fun getStats() = statMap

    override fun getPolymerEntityType(): EntityType<ZombieEntity> = EntityType.ZOMBIE

    override fun getEquippedStack(slot: EquipmentSlot?): ItemStack = ItemStack.EMPTY

    override fun getArmorItems() = listOf<ItemStack>()

    override fun equipStack(slot: EquipmentSlot?, stack: ItemStack?) {
    }

    override fun getMainArm() = Arm.RIGHT
}