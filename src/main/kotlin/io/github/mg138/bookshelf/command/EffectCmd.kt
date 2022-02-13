package io.github.mg138.bookshelf.command

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType.getDouble
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.IntegerArgumentType.getInteger
import com.mojang.brigadier.context.CommandContext
import io.github.mg138.bookshelf.command.suggestion.EffectSuggestion
import io.github.mg138.bookshelf.effect.ActiveEffectManager
import io.github.mg138.bookshelf.effect.StatusEffectManager
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.command.argument.EntityArgumentType.getEntity
import net.minecraft.command.argument.IdentifierArgumentType
import net.minecraft.command.argument.IdentifierArgumentType.getIdentifier
import net.minecraft.entity.LivingEntity
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText


object EffectCmd {
    private fun listEffects(context: CommandContext<ServerCommandSource>): Int {
        val source = context.source
        source.sendFeedback(LiteralText("Effects:"), false)

        StatusEffectManager.effects.forEach { (id, _) ->
            source.sendFeedback(
                LiteralText(id.toString()),
                false
            )
        }

        return Command.SINGLE_SUCCESS
    }

    private fun applyEffect(context: CommandContext<ServerCommandSource>): Int {
        val entity = getEntity(context, "target")
        if (entity !is LivingEntity) return 0

        val effectId = getIdentifier(context, "effect")
        val duration = getInteger(context, "duration")
        val power = getDouble(context, "power")
        val effect = StatusEffectManager.get(effectId) ?: return 0

        ActiveEffectManager.addEffect(entity, duration, power, effect)

        return Command.SINGLE_SUCCESS
    }

    fun register() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            dispatcher.register(
                literal("book")
                    .then(
                        literal("effect")
                            .then(
                                literal("list_ids")
                                    .executes(::listEffects)
                            )
                            .then(
                                literal("apply")
                                    .then(
                                        argument("target", EntityArgumentType.entity())
                                            .then(
                                                argument("effect", IdentifierArgumentType.identifier())
                                                    .suggests(EffectSuggestion)
                                                    .then(
                                                        argument("duration", IntegerArgumentType.integer())
                                                            .then(
                                                                argument("power", DoubleArgumentType.doubleArg())
                                                                    .executes(::applyEffect)
                                                            )
                                                    )
                                            )
                                    )
                            )
                    )
            )
        }
    }
}