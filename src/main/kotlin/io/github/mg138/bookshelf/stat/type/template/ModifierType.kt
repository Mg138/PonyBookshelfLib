package io.github.mg138.bookshelf.stat.type.template

import io.github.mg138.bookshelf.stat.type.StatType
import net.minecraft.util.Identifier

//abstract class ModifierType(id: Identifier) :
//    StatType(id) {
//    override val damagePriority = 1
//
//    abstract class ModifierTypeTemplate(
//        id: Identifier
//    ) : ModifierType(id) {
//        abstract fun condition(it: Double, event: BookDamageEvent): Boolean
//
//        override fun onDamage(it: Double, event: BookDamageEvent) {
//            if (condition(it, event)) {
//                event.damagerStat.map { (type, stat) ->
//                    if (type is DamageType) {
//                        stat * (it / 100)
//                    }
//                }
//            }
//        }
//    }
//}