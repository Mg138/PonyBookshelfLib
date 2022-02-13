package io.github.mg138.bookshelf.effect

abstract class BookStatusEffect {
    abstract fun merge(a: BookStatusEffectInfo, b: BookStatusEffectInfo): BookStatusEffectInfo

    open fun start(effect: BookStatusEffectInfo) {
    }

    open fun act(effect: BookStatusEffectInfo, currentTime: Long) {
    }

    open fun end(effect: BookStatusEffectInfo) {
    }
}