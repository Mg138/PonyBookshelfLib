package io.github.mg138.bookshelf.mixins;

import io.github.mg138.bookshelf.damage.DamageManager;
import io.github.mg138.bookshelf.entity.StatedEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.EntityDamageSource;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(
            method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    public void pony_bookshelf_damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!(source instanceof EntityDamageSource)) {
            final LivingEntity entity = (LivingEntity) (Object) this;

            if (entity instanceof StatedEntity statedEntity) {
                final ActionResult result = DamageManager.INSTANCE.damage(entity, statedEntity.getStats(), null, null, source);

                if (result != ActionResult.PASS) {
                    cir.setReturnValue(false);
                }
                cir.setReturnValue(true);
            }
        }
    }
}
