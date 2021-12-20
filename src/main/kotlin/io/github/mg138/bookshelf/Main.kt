package io.github.mg138.bookshelf

import io.github.mg138.bookshelf.command.item.ItemCmd
import io.github.mg138.bookshelf.config.ItemConfig
import io.github.mg138.bookshelf.item.ServerItemManager
import io.github.mg138.bookshelf.item.event.ItemEventsHandler
import io.github.mg138.bookshelf.item.test.FabricItem
import io.github.mg138.rloader.resource.ResourceHelper
import net.fabricmc.api.DedicatedServerModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.registry.Registry
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteExisting
import kotlin.io.path.deleteIfExists


@Suppress("UNUSED")
object Main : DedicatedServerModInitializer {
    const val modId = "pony_bookshelf"
    val logger: Logger = LogManager.getLogger(modId)
    val fabricLoader = FabricLoader.getInstance()
    val modContainer = fabricLoader.getModContainer(modId).get()

    val serverDir = fabricLoader.gameDir
    val resourceDir = serverDir.resolve("pony_bookshelf_resource")
        .also { it.toFile().deleteRecursively() }
        .also { it.createDirectories() }
    val assetsDir = resourceDir.resolve("assets")
        .also { it.createDirectories() }

    val serverItemConfig = ItemConfig.register()

    override fun onInitializeServer() {
        if (serverItemConfig.test) {
            ServerItemManager.register(FabricItem())
            logger.info("Item test enabled.")
        }

        ResourceHelper.register(resourceDir)
        ItemEventsHandler.register()
        ItemCmd.register()
    }
}