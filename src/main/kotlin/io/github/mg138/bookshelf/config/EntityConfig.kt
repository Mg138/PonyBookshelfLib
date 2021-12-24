package io.github.mg138.bookshelf.config

import io.github.mg138.bookshelf.Main
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment

@Config(name = "${Main.modId}/entity_config")
class EntityConfig : ConfigData {
    @Comment("Server entity test.")
    var test = true

    companion object {
        private val clazz = EntityConfig::class.java

        fun register(): EntityConfig {
            return AutoConfig.register(clazz, ConfigUtil::serializer).config
        }
    }
}