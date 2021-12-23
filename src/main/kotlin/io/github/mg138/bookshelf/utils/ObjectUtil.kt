package io.github.mg138.bookshelf.utils

import java.lang.reflect.Modifier
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.javaGetter

object ObjectUtil {
    fun Int.isPublic() = Modifier.isPublic(this)

    inline fun <reified T : Any, reified O : Any> getFieldsOfObject(): List<T> {
        val clazz = O::class
        val instance = clazz.objectInstance ?: return emptyList()

        return clazz.declaredMemberProperties
            .filter { it.isFinal }
            .filter { it.javaGetter?.modifiers?.isPublic() ?: false }
            .map { it.get(instance) }
            .filterIsInstance<T>()
    }
}