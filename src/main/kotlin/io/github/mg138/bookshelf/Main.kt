package io.github.mg138.bookshelf

import eu.pb4.polymer.api.resourcepack.PolymerRPUtils
import io.github.mg138.bookshelf.command.ItemCmd
import io.github.mg138.bookshelf.command.StatTypeCmd
import io.github.mg138.bookshelf.config.ItemConfig
import io.github.mg138.bookshelf.damage.DamageIndicatorManager
import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.item.ServerItemManager
import io.github.mg138.bookshelf.item.test.FabricItem
import io.github.mg138.bookshelf.stat.type.Preset
import io.github.mg138.bookshelf.stat.type.StatTypeManager
import net.fabricmc.api.DedicatedServerModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Suppress("UNUSED")
object Main : DedicatedServerModInitializer {
    const val modId = "pony_bookshelf"
    val logger: Logger = LogManager.getLogger(modId)

    val serverItemConfig = ItemConfig.register()

    override fun onInitializeServer() {
        PolymerRPUtils.addAssetSource(modId)
        Preset.types.forEach(StatTypeManager::register)

        if (serverItemConfig.test) {
            ServerItemManager.register(FabricItem())
            logger.info("Item test enabled.")
        }

        DamageIndicatorManager.register()
        DamageManager.register()
        ItemCmd.register()
        StatTypeCmd.register()
    }
}