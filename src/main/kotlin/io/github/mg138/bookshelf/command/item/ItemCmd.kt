package io.github.mg138.bookshelf.command.item

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import io.github.mg138.bookshelf.item.ServerItemManager
import io.github.mg138.bookshelf.item.command.ItemIdSuggestion
import io.github.mg138.bookshelf.item.nbt.VanillaItemNbt
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import net.minecraft.util.Identifier


object ItemCmd {
    private fun get(context: CommandContext<ServerCommandSource>): Int {
        val id = context.getArgument(ItemIdSuggestion.id, Identifier::class.java)

        ServerItemManager.getAsItemStack(id)?.let {
            context.source.player.giveItemStack(it)
        }

        return Command.SINGLE_SUCCESS
    }

    private fun listIds(context: CommandContext<ServerCommandSource>): Int {
        val source = context.source
        source.sendFeedback(LiteralText("IDs:"), false)

        ServerItemManager.ids.forEach { id ->
            source.sendFeedback(LiteralText(id.toString()), false)
        }

        return Command.SINGLE_SUCCESS
    }

    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                literal("book")
                    .then(
                        literal("item")
                            .then(
                                literal("get")
                                    .then(
                                        argument(ItemIdSuggestion.id, IdentifierArgumentType.identifier())
                                            .suggests(ItemIdSuggestion)
                                            .executes(this::get)
                                    )
                            )
                            .then(
                                literal("list_ids")
                                    .executes(this::listIds)
                            )
                    )
            )
        }
    }
}