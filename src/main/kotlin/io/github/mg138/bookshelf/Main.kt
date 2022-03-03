package io.github.mg138.bookshelf

import eu.pb4.polymer.api.resourcepack.PolymerRPUtils
import io.github.mg138.bookshelf.command.EffectCmd
import io.github.mg138.bookshelf.command.StatTypeCmd
import io.github.mg138.bookshelf.config.EntityConfig
import io.github.mg138.bookshelf.config.ItemConfig
import io.github.mg138.bookshelf.damage.DamageIndicatorManager
import io.github.mg138.bookshelf.damage.DamageManager
import io.github.mg138.bookshelf.effect.ActiveEffectManager
import io.github.mg138.bookshelf.effect.impl.Bleeding
import io.github.mg138.bookshelf.effect.impl.Burning
import io.github.mg138.bookshelf.effect.impl.FallResistance
import io.github.mg138.bookshelf.entity.impl.DummyEntity
import io.github.mg138.bookshelf.entity.impl.TestZombie
import io.github.mg138.bookshelf.item.test.FabricItem
import io.github.mg138.bookshelf.projectile.ArrowProjectile
import io.github.mg138.bookshelf.player.PlayerSidebar
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.stat.type.StatTypes
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

        StatTypes.types.forEach(StatType::register)

        Bleeding.register()
        Burning.register()
        FallResistance.register()

        ActiveEffectManager.register()

        if (serverItemConfig.test) {
            FabricItem.register()
            logger.info("Item test enabled.")
        }

        DummyEntity.register()
        if (serverEntityConfig.test) {
            TestZombie.register()
            logger.info("Entity test enabled.")
        }
        ArrowProjectile.register()

        DamageIndicatorManager.register()
        DamageManager.register()

        StatTypeCmd.register()
        EffectCmd.register()

        PlayerSidebar.register()
    }
}