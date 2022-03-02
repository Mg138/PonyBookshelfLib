package io.github.mg138.bookshelf.mixins;

import io.github.mg138.bookshelf.entity.StatedEntity;
import io.github.mg138.bookshelf.stat.data.MutableStats;
import io.github.mg138.bookshelf.stat.data.PlayerStatMap;
import io.github.mg138.bookshelf.stat.stat.Stat;
import io.github.mg138.bookshelf.stat.stat.StatSingle;
import io.github.mg138.bookshelf.stat.type.StatType;
import io.github.mg138.bookshelf.stat.type.StatTypes;
import io.github.mg138.bookshelf.utils.PlayerUtil;
import kotlin.Pair;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements StatedEntity {
    PlayerStatMap pony_bookshelf_playerStatMap = null;

    @NotNull
    @Override
    public MutableStats getStats() {
        if (pony_bookshelf_playerStatMap == null) {
            pony_bookshelf_playerStatMap = new PlayerStatMap((ServerPlayerEntity) (Object) this);

            pony_bookshelf_playerStatMap.putStat(StatTypes.MiscTypes.MaxHealth.INSTANCE, new StatSingle(100.0));
        }

        return pony_bookshelf_playerStatMap;
    }

    @Nullable
    @Override
    public Stat getStat(@NotNull StatType type) {
        return getStats().getStat(type);
    }

    @Override
    public double getStatResult(@NotNull StatType type) {
        return getStats().getStatResult(type);
    }

    @NotNull
    @Override
    public Set<StatType> types() {
        return getStats().types();
    }

    @NotNull
    @Override
    public Collection<Stat> stats() {
        return getStats().stats();
    }

    @NotNull
    @Override
    public List<Pair<StatType, Stat>> pairs() {
        return getStats().pairs();
    }
}
