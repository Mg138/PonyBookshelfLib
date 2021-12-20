package io.github.mg138.bookshelf.item.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import io.github.mg138.bookshelf.Main
import io.github.mg138.bookshelf.item.ServerItem
import net.minecraft.item.Item
import net.minecraft.util.Identifier
import java.nio.file.Path

class Asset(val model: Model, val texture: Texture) {
    data class Model(val modelPath: String, val parentPath: Path, val id: Identifier, val vanillaItem: Item, val customModelData: Int) {
        companion object {
            val gson: Gson = GsonBuilder().registerTypeAdapter(Model::class.java, object : TypeAdapter<Model>() {
                override fun write(writer: JsonWriter, value: Model) {
                    val id = value.id

                    writer.beginObject().apply {
                        name("parent").value("item/generated")

                        name("textures").beginObject().apply {
                            name("layer0").value("item/${value.vanillaItem}")
                        }.endObject()

                        name("overrides").beginArray().apply {
                            beginObject().apply {
                                name("predicate").beginObject().apply {
                                    name("custom_model_data").value(value.customModelData)
                                }.endObject()

                                name("model").value("${id.namespace}:item/${id.path}")
                            }.endObject()
                        }.endArray()
                    }.endObject()
                }

                override fun read(reader: JsonReader): Model {
                    throw IllegalArgumentException("Read not supported")
                }
            }).create()
        }

        fun toJson(): String = gson.toJson(this)
    }

    data class Texture(val texturePath: String, val parentPath: Path)

    companion object {
        fun copyTexture(texture: Texture) {
            val texturePath = texture.texturePath

            texture.parentPath.resolve(texturePath).toFile().copyRecursively(
                Main.resourceDir.resolve(texturePath).toFile(), true
            )
        }

        fun copyModel(serverItem: ServerItem) {
            val model = serverItem.config.assets.model
            val modelPath = model.modelPath
            val vanillaModel = Main.assetsDir.resolve("minecraft/models/item/${serverItem.vanillaItem}.json").toFile()

            vanillaModel.parentFile.mkdirs()
            vanillaModel.writeText(model.toJson())

            model.parentPath.resolve(modelPath).toFile().copyRecursively(
                Main.resourceDir.resolve(modelPath).toFile(), true
            )
        }
    }
}