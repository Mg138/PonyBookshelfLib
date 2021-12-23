package io.github.mg138.bookshelf.utils

import net.minecraft.util.Identifier

operator fun String.minus(path: String) = Identifier(this, path)