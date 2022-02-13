package io.github.mg138.bookshelf.command.suggestion

import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.suggestion.SuggestionProvider
import com.mojang.brigadier.suggestion.Suggestions
import com.mojang.brigadier.suggestion.SuggestionsBuilder
import io.github.mg138.bookshelf.effect.StatusEffectManager
import net.minecraft.server.command.ServerCommandSource
import java.util.concurrent.CompletableFuture

object EffectSuggestion : SuggestionProvider<ServerCommandSource> {
    override fun getSuggestions(
        context: CommandContext<ServerCommandSource>?,
        builder: SuggestionsBuilder
    ): CompletableFuture<Suggestions> {
        StatusEffectManager.effects
            .forEach { builder.suggest(it.first.toString()) }
        return builder.buildFuture()
    }
}