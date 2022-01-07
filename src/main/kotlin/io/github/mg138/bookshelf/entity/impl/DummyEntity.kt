package io.github.mg138.bookshelf.entity.impl

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.entity.BookStaticStatedEntity
import io.github.mg138.bookshelf.stat.data.StatMap
import io.github.mg138.bookshelf.stat.stat.StatSingle
import io.github.mg138.bookshelf.stat.type.StatTypes
import io.github.mg138.bookshelf.utils.minus
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.SpawnGroup
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.MobEntity.createMobAttributes
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.MinecraftServer
import net.minecraft.text.LiteralText
import net.minecraft.util.Arm
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import kotlin.jvm.internal.Ref
import kotlin.math.roundToInt

class DummyEntity(type: EntityType<DummyEntity>, world: World) :
    BookStaticStatedEntity<DummyEntity>(type, world, StatMap().apply {
        putStat(StatTypes.DefenseTypes.DEFENSE_AQUA, StatSingle(2000.0))
    }) {
    companion object {
        val map: MutableMap<PlayerEntity, Pair<Ref.IntRef, MutableList<Float>>> = mutableMapOf()

        val DUMMY: EntityType<DummyEntity> = Registry.register(
            Registry.ENTITY_TYPE,
            Main.modId - "test_dummy_entity",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::DummyEntity)
                .dimensions(EntityType.VILLAGER.dimensions)
                .disableSaving()
                .build()
        )

        private fun tick(server: MinecraftServer) {
            val toRemove: MutableList<PlayerEntity> = mutableListOf()

            map.forEach { (player, pair) ->
                val age = pair.first.apply { element++ }.element

                if (age >= 200) {
                    toRemove += player
                }

                val list = pair.second
                list[age % 20] = 0.0F

                val avg = list.sum().roundToInt() / list.size
                val str = "DPS: $avg"
                player.sendMessage(LiteralText(str), true)
            }

            toRemove.forEach {
                map.remove(it)
            }
        }

        fun register() {
            FabricDefaultAttributeRegistry.register(DUMMY, createMobAttributes())

            ServerTickEvents.END_SERVER_TICK.register(this::tick)

            ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
                map.remove(handler.player)
            }
        }
    }

    override fun getPolymerEntityType(): EntityType<VillagerEntity> = EntityType.VILLAGER

    override fun getEquippedStack(slot: EquipmentSlot?): ItemStack = ItemStack.EMPTY

    override fun getArmorItems() = listOf<ItemStack>()

    override fun equipStack(slot: EquipmentSlot?, stack: ItemStack?) {
        throw IllegalArgumentException("Dummy shouldn't have armor")
    }

    override fun getMainArm() = Arm.RIGHT

    override fun isPushable() = false

    override fun isPushedByFluids() = false

    override fun pushAway(entity: Entity?) {
    }

    override fun pushAwayFrom(entity: Entity?) {
    }

    override fun applyDamage(source: DamageSource, amount: Float) {
        val damager = source.attacker
        if (damager !is PlayerEntity) return

        val (age, list) = map.getOrPut(damager) {
            Pair(
                Ref.IntRef().apply { element = 0 },
                ArrayList<Float>().apply {
                    for (i in 0 until 20) {
                        this.add(0.0F)
                    }
                }
            )
        }
        val time = age.element % 20
        list[time] += amount
    }

    override fun takeKnockback(strength: Double, x: Double, z: Double) {
    }
}