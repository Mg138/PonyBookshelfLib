package io.github.mg138.bookshelf.mixins;

import io.github.mg138.bookshelf.damage.BookDamageSource;
import io.github.mg138.bookshelf.damage.BookDamageable;
import io.github.mg138.bookshelf.damage.DamageManager;
import io.github.mg138.bookshelf.entity.StatedEntity;
import io.github.mg138.bookshelf.stat.stat.Stat;
import io.github.mg138.bookshelf.stat.stat.StatSingle;
import io.github.mg138.bookshelf.stat.type.StatTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements BookDamageable {
    @Shadow
    public abstract float getMaxHealth();

    @Redirect(
            method = "applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setHealth(F)V")
    )
    private void pony_bookshelf_fakeSetHealth(LivingEntity instance, float health) {
        if (!(instance instanceof StatedEntity)) {
            instance.setHealth(health);
        }
    }

    @Inject(
            method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z",
            at = @At("TAIL"),
            cancellable = true
    )
    public void pony_bookshelf_damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (amount > 0.0F && !(source instanceof BookDamageSource)) {
            final LivingEntity entity = (LivingEntity) (Object) this;

            if (entity instanceof StatedEntity statedEntity) {
                final ActionResult result = DamageManager.INSTANCE.vanillaDamage(entity, statedEntity.getStats(), amount, source);

                if (result != ActionResult.PASS) {
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Override
    public void bookDamage(double amount, @NotNull DamageSource source) {
        BookDamageable.Companion.defaultBookDamage(amount, source, this);
    }

    @Inject(
            method = "getHealth()F",
            at = @At("HEAD"),
            cancellable = true
    )
    public void pony_bookshelf_getHealth(CallbackInfoReturnable<Float> cir) {
        final LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof StatedEntity statedEntity) {
            try {
                Stat maxHealthStat = statedEntity.getStat(StatTypes.MiscTypes.MaxHealth.INSTANCE);
                if (maxHealthStat == null) return;

                Stat healthStat = statedEntity.getStat(StatTypes.MiscTypes.Health.INSTANCE);
                if (healthStat == null) {
                    healthStat = new StatSingle(maxHealthStat.result());
                    statedEntity.getStats().putStat(StatTypes.MiscTypes.Health.INSTANCE, healthStat);
                }

                float newHealth = (float) (healthStat.result() / maxHealthStat.result() * this.getMaxHealth());

                cir.setReturnValue(newHealth);
            } catch (Exception ignored) {
            }
        }
    }

    @Inject(
            method = "setHealth(F)V",
            at = @At("HEAD")
    )
    public void pony_bookshelf_setHealth(float health, CallbackInfo ci) {
        if (health <= 0.0F) return;

        final LivingEntity entity = (LivingEntity) (Object) this;

        if (entity instanceof StatedEntity statedEntity) {
            try {
                Stat maxHealthStat = statedEntity.getStat(StatTypes.MiscTypes.MaxHealth.INSTANCE);
                if (maxHealthStat == null) return;

                double newHealth = maxHealthStat.result() * (((double) health) / this.getMaxHealth());

                statedEntity.getStats().putStat(StatTypes.MiscTypes.Health.INSTANCE, new StatSingle(Math.min(newHealth, maxHealthStat.result())));
            } catch (Exception ignored) {
            }
        }
    }
}
