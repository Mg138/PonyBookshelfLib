package io.github.mg138.bookshelf.player

import eu.pb4.sidebars.api.Sidebar
import io.github.mg138.bookshelf.entity.StatedEntity
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.utils.healthStat
import io.github.mg138.bookshelf.utils.maxHealthStat
import io.github.mg138.bookshelf.utils.percentColor
import io.github.mg138.bookshelf.utils.toLiteralText
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.TranslatableText

object PlayerSidebar {
    private fun healthColor(health: Stat, maxHealth: Stat) =
        percentColor(health.result() / maxHealth.result())

    private fun healthToText(health: Stat, maxHealth: Stat) = health.result()
        .times(10.0)
        .toInt()
        .div(10.0)
        .toString()
        .toLiteralText()
        .styled { it.withColor(healthColor(health, maxHealth)) }

    private fun Sidebar.health(player: ServerPlayerEntity) {
        if (player is StatedEntity) {
            val health = player.healthStat() ?: return
            val maxHealth = player.maxHealthStat() ?: return

            this.addLines(TranslatableText("pony_bookshelf.player.info.text.health", healthToText(health, maxHealth)))
        }
    }

    fun register() {
        ServerTickEvents.START_WORLD_TICK.register { world ->
            world.players.forEach { player ->
                val sideBar = Sidebar(Sidebar.Priority.LOW)

                sideBar.title = TranslatableText("pony_bookshelf.player.info.title")
                sideBar.health(player)

                sideBar.addPlayer(player)

                sideBar.show()
            }
        }
        /*
        ServerPlayerEvents.AFTER_RESPAWN.register { _, newPlayer, _ ->
            if (newPlayer is StatedEntity) {
                newPlayer.healthStat()
            }
        }
         */
    }
}