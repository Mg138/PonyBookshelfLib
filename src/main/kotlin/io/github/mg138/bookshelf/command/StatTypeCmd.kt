package io.github.mg138.bookshelf.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import io.github.mg138.bookshelf.stat.type.StatTypeManager
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText


object StatTypeCmd {
    private fun listStatTypes(context: CommandContext<ServerCommandSource>): Int {
        val source = context.source
        source.sendFeedback(LiteralText("StatTypes:"), false)

        StatTypeManager.registeredTypes.forEach { id ->
            source.sendFeedback(
                id.name()
                    .append(" (Indicator: ")
                    .append(id.indicator())
                    .append(") "),
                false
            )
        }

        return Command.SINGLE_SUCCESS
    }

    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                literal("book")
                    .then(
                        literal("stat_types")
                            .then(
                                literal("list_ids")
                                    .executes(this::listStatTypes)
                            )
                    )
            )
        }
    }
}