package io.github.mg138.bookshelf.item.event

import io.github.mg138.bookshelf.item.BookItem
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.event.player.UseBlockCallback
import net.fabricmc.fabric.api.event.player.UseEntityCallback
import net.minecraft.util.ActionResult

object BookItemPrevention {
    fun register() {
        AttackBlockCallback.EVENT.register { player, _, hand, _, _ ->
            if (player.getStackInHand(hand).item is BookItem) {
                ActionResult.FAIL
            } else {
                ActionResult.PASS
            }
        }
        UseBlockCallback.EVENT.register { player, _, hand, _ ->
            if (player.getStackInHand(hand).item is BookItem) {
                ActionResult.FAIL
            } else {
                ActionResult.PASS
            }
        }
        UseEntityCallback.EVENT.register { player, _, hand, _, _ ->
            if (player.getStackInHand(hand).item is BookItem) {
                ActionResult.FAIL
            } else {
                ActionResult.PASS
            }
        }
    }
}