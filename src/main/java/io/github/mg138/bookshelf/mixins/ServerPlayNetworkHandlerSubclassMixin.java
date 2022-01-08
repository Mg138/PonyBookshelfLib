package io.github.mg138.bookshelf.mixins;

import io.github.mg138.bookshelf.item.BookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/server/network/ServerPlayNetworkHandler$1")
public abstract class ServerPlayNetworkHandlerSubclassMixin {
    @Shadow @Final
    ServerPlayNetworkHandler field_28963;

    @Inject(
            method = "attack()V",
            at = @At("HEAD"),
            cancellable = true
    )
    public void pony_bookshelf_attack(CallbackInfo ci) {
        ServerPlayNetworkHandler handler = field_28963;
        ServerPlayerEntity player = handler.player;
        ItemStack itemStack = player.getMainHandStack();
        Item item = itemStack.getItem();

        if (item instanceof BookItem) {
            ci.cancel();
        }
    }
}
