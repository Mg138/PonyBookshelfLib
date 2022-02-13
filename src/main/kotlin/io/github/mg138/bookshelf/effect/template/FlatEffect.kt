package io.github.mg138.bookshelf.effect.template

import io.github.mg138.bookshelf.effect.BookStatusEffectInfo
import io.github.mg138.bookshelf.effect.BookStatusEffect


abstract class FlatEffect : BookStatusEffect() {
    override fun merge(a: BookStatusEffectInfo, b: BookStatusEffectInfo): BookStatusEffectInfo {
        return a.copy(
            duration=(a.duration + b.duration) / 2,
            power=(a.power + b.power) / 2
        )
    }
}