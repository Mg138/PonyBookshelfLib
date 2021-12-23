package io.github.mg138.bookshelf.mixins

import io.github.mg138.bookshelf.damage.DamageIndicatorManager
import net.minecraft.entity.decoration.ArmorStandEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Redirect


@Mixin(ArmorStandEntity::class)
class IndicatorMixin {
    @Redirect(
        method = ["travel"],
        at = At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/decoration/ArmorStandEntity;canClip()Z",
            remap = true
        )
    )
    fun modifiedCanClip(e: ArmorStandEntity): Boolean {
        if (e is DamageIndicatorManager.Indicator) return true

        return !e.isMarker && !e.hasNoGravity()
    }
}