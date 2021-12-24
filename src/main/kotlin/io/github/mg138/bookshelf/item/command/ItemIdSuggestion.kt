package io.github.mg138.bookshelf.item.command

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.mg138.bookshelf.item.ServerItemManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.util.Identifier
import java.util.concurrent.CompletableFuture

object ItemIdSuggestion : SuggestionProvider<ServerCommandSource> {
    const val id = "item_id"

    private val ids
        get() = ServerItemManager.ids.map { it.toString() }

    override fun getSuggestions(
        context: CommandContext<ServerCommandSource>,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        val id = try {
            context.getArgument(id, Identifier::class.java).path
        } catch (e: Exception) {
            ""
        }

        ids
            .filter { id in it }
            .forEach { builder.suggest(it) }

        return builder.buildFuture()
    }
}