package io.github.mg138.bookshelf.entity.impl

import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.damage.BookDamageable
import io.github.mg138.bookshelf.entity.BookEntity
import io.github.mg138.bookshelf.entity.bookAttributes
import io.github.mg138.bookshelf.utils.minus
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.entity.*
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.passive.VillagerEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.text.LiteralText
import net.minecraft.util.Arm
import net.minecraft.util.registry.Registry
import net.minecraft.world.World
import kotlin.jvm.internal.Ref
import kotlin.math.roundToInt

class DummyEntity(type: EntityType<DummyEntity>, world: World) :
    LivingEntity(type, world), BookEntity, BookDamageable {
    companion object {
        val map: MutableMap<PlayerEntity, Pair<Ref.IntRef, MutableList<Double>>> = mutableMapOf()

        val DUMMY: EntityType<DummyEntity> = Registry.register(
            Registry.ENTITY_TYPE,
            Main.modId - "test_dummy_entity",
            FabricEntityTypeBuilder.create(SpawnGroup.MISC, ::DummyEntity)
                .dimensions(EntityType.VILLAGER.dimensions)
                .disableSaving()
                .build()
        )

        private fun avg(list: MutableList<Double>): Double {
            return list
                .sum()
                .times(100.0)
                .div(list.size)
                .roundToInt()
                .div(100.0)
        }

        private fun tick() {
            val toRemove: MutableList<PlayerEntity> = mutableListOf()

            map.forEach { (player, pair) ->
                val age = pair.first.apply { element++ }.element

                if (age >= 200) {
                    toRemove += player
                }

                val list = pair.second
                list[age % 20] = 0.0

                val avg = avg(list)
                val str = "DPS: $avg"
                player.sendMessage(LiteralText(str), true)
            }

            toRemove.forEach {
                map.remove(it)
            }
        }

        fun register() {
            ServerTickEvents.END_SERVER_TICK.register {
                this.tick()
            }

            ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
                map.remove(handler.player)
            }

            FabricDefaultAttributeRegistry.register(DUMMY, bookAttributes())
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

    override fun takeKnockback(strength: Double, x: Double, z: Double) {
    }

    override fun applyDamage(source: DamageSource?, amount: Float) {
    }

    override fun bookDamage(amount: Double, source: DamageSource) {
        super.bookDamage(amount, source)

        val damager = source.source
        if (damager !is PlayerEntity) return

        val (age, list) = map.getOrPut(damager) {
            Pair(
                Ref.IntRef().apply { element = 0 },
                ArrayList<Double>().apply {
                    for (i in 0 until 20) {
                        this.add(0.0)
                    }
                }
            )
        }
        val time = age.element % 20
        list[time] += amount
    }
}