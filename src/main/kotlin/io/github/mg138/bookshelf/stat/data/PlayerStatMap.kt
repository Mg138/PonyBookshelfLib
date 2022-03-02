package io.github.mg138.bookshelf.stat.data

import io.github.mg138.bookshelf.item.type.StatedItem
import io.github.mg138.bookshelf.stat.stat.Stat
import io.github.mg138.bookshelf.stat.type.StatType
import io.github.mg138.bookshelf.utils.PlayerUtil
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.commons.lang3.tuple.MutablePair

class PlayerStatMap(private val player: ServerPlayerEntity) : MutableStats {
    private val itemStatsCache: MutablePair<Map<ItemStack, StatedItem>, Stats> = MutablePair(mutableMapOf(), StatMap())

    private val map: MutableStats = StatMap()

    private fun getActualStats(): Stats {
        val items = PlayerUtil.getItems(player)

        if (itemStatsCache.left != items) {
            itemStatsCache.setRight(StatMap().also {
                items.forEach { (itemStack, item) ->
                    val stats = item.getStats(itemStack)

                    it.addAll(stats)
                }
            })
            itemStatsCache.setLeft(items)
        }

        return map.copy().apply {
            addAll(itemStatsCache.right)
        }
    }

    override fun addStat(type: StatType, stat: Stat) = map.addStat(type, stat)

    override fun addAll(stats: Stats?) = map.addAll(stats)

    override fun putStat(type: StatType, stat: Stat) = map.putStat(type, stat)

    override fun putStat(stat: Pair<StatType, Stat>) = map.putStat(stat)

    override fun remove(type: StatType) = map.remove(type)

    override fun copy() = this.getActualStats().copy()

    override fun getStatResult(type: StatType) = this.getActualStats().getStatResult(type)

    override fun getStat(type: StatType) = this.getActualStats().getStat(type)

    override fun has(type: StatType) = this.getActualStats().has(type)

    override fun types() = this.getActualStats().types()

    override fun stats() = this.getActualStats().stats()

    override fun pairs() = this.getActualStats().pairs()

    override fun lores() = this.getActualStats().lores()

    override fun iterator() = this.getActualStats().iterator()

    override fun toString() = this.getActualStats().toString()
}