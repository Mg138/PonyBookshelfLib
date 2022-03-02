package io.github.mg138.bookshelf.mixins;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin {
    @Redirect(
            method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setHealth(F)V")
    )
    private void pony_bookshelf_fakeSetHealth(PlayerEntity instance, float v) {
    }
}
