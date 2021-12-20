package io.github.mg138.bookshelf.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer

object ConfigUtil {
    fun <T : ConfigData> serializer(definition: Config, configClass: Class<T>) =
        JanksonConfigSerializer(definition, configClass)
}