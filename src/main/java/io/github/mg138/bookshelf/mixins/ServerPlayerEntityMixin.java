package io.github.mg138.bookshelf.mixins;

import io.github.mg138.bookshelf.entity.StatedEntity;
import io.github.mg138.bookshelf.stat.data.MutableStats;
import io.github.mg138.bookshelf.stat.data.StatMap;
import io.github.mg138.bookshelf.stat.data.Stats;
import io.github.mg138.bookshelf.utils.PlayerUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements StatedEntity {
    @NotNull
    @Override
    public MutableStats getStats() {
        return PlayerUtil.INSTANCE.getStats((ServerPlayerEntity) (Object) this);
    }
}
