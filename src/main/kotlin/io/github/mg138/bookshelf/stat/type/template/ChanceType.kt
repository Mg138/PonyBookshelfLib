package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.util.Identifier
import java.util.*

//abstract class ChanceType(id: Identifier) :
//    StatType(id) {
//    override val damagePriority = 100
//
//    companion object {
//        private val rand = Random()
//
//        private fun roll(chance: Double): Boolean = rand.nextDouble() <= (chance % 1)
//
//        fun roundChance(chance: Double) = chance.toInt().let {
//            if (roll(chance)) {
//                it + 1
//            } else {
//                it
//            }
//        }
//    }
//
//    abstract class ChanceTypeTemplate(
//        id: Identifier,
//        val powerType: PowerType
//    ) : ChanceType(id) {
//        override fun onDamage(it: Double, event: BookDamageEvent) {
//            // power's calculation is supposedly less expensive, so we're doing that first.
//
//            val power = event.damagerStat[powerType]?.result() ?: return
//            if (power <= 0.0) return
//            // power should always be positive
//            // otherwise it shouldn't have an effect at all
//
//            val chance = roundChance(it)
//            if (chance < 1) return
//            // chance should always be >= 1
//            // if you have -1 chance or 0 chance
//            // then you definitely shouldn't activate the power.
//
//            powerType.onDamage(power * chance, event)
//        }
//    }
//}