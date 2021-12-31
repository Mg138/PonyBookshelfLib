package io.github.mg138.bookshelf

import eu.pb4.polymer.api.resourcepack.PolymerRPUtils
import io.github.mg138.bookshelf.command.StatTypeCmd
import io.github.mg138.bookshelf.config.EntityConfig
import io.github.mg138.bookshelf.config.ItemConfig
import io.github.mg138.bookshelf.damage.DamageIndicatorManager
import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.effect.Bleeding
import io.github.mg138.bookshelf.effect.Burning
import io.github.mg138.bookshelf.entity.impl.DummyEntity
import io.github.mg138.bookshelf.item.test.FabricItem
import io.github.mg138.bookshelf.stat.type.Preset
import io.github.mg138.bookshelf.stat.type.StatTypeManager
import net.fabricmc.api.DedicatedServerModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

@Suppress("UNUSED")
object Main : DedicatedServerModInitializer {
    const val modId = "pony_bookshelf"
    private val logger: Logger = LogManager.getLogger(modId)

    private val serverItemConfig = ItemConfig.register()
    private val serverEntityConfig = EntityConfig.register()

    override fun onInitializeServer() {
        PolymerRPUtils.addAssetSource(modId)
        Preset.types.forEach(StatTypeManager::register)
        Bleeding.register()
        Burning.register()

        if (serverItemConfig.test) {
            FabricItem.register()
            logger.info("Item test enabled.")
        }

        if (serverEntityConfig.test) {
            DummyEntity.register()
            logger.info("Entity test enabled.")
        }

        DamageIndicatorManager.register()
        DamageManager.register()
        StatTypeCmd.register()
    }
}