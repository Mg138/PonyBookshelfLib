package io.github.mg138.bookshelf.item.event

import io.github.mg138.bookshelf.item.ServerItemManager
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.AttackEntityCallback
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.item.ItemStack
import net.minecraft.util.ActionResult
import net.minecraft.util.TypedActionResult

object ItemEventsHandler {
    fun register() {
        AttackBlockCallback.EVENT.register attackBlock@{ player, world, hand, pos, direction ->
            player.itemsHand
                .mapNotNull { ServerItemManager.get(it) }
                .forEach {
                    val result = it.onAttackBlock(player, world, hand, pos, direction)

                    if (result != ActionResult.PASS) return@attackBlock result
                }
            ActionResult.PASS
        }

        AttackEntityCallback.EVENT.register attackEntity@{ player, world, hand, entity, hitResult ->
            player.itemsHand
                .mapNotNull { ServerItemManager.get(it) }
                .forEach {
                    val result = it.onAttackEntity(player, world, hand, entity, hitResult)

                    if (result != ActionResult.PASS) return@attackEntity result
                }
            ActionResult.PASS
        }

        UseBlockCallback.EVENT.register useBlock@{ player, world, hand, hitResult ->
            player.itemsHand
                .mapNotNull { ServerItemManager.get(it) }
                .forEach {
                    val result = it.onUseBlock(player, world, hand, hitResult)

                    if (result != ActionResult.PASS) return@useBlock result
                }
            ActionResult.PASS
        }

        UseEntityCallback.EVENT.register useEntity@{ player, world, hand, entity, hitResult ->
            player.itemsHand
                .mapNotNull { ServerItemManager.get(it) }
                .forEach {
                    val result = it.onUseEntity(player, world, hand, entity, hitResult)

                    if (result != ActionResult.PASS) return@useEntity result
                }
            ActionResult.PASS
        }

        UseItemCallback.EVENT.register useItem@{ player, world, hand ->
            player.itemsHand
                .mapNotNull { ServerItemManager.get(it) }
                .forEach {
                    val result = it.onUse(player, world, hand)

                    if (result.result != ActionResult.PASS) return@useItem result
                }
            TypedActionResult.pass(ItemStack.EMPTY)
        }
    }
}