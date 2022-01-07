package io.github.mg138.bookshelf.mixins;

import io.github.mg138.bookshelf.item.BookItem;
import io.github.mg138.bookshelf.item.type.MeleeWeapon;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Inject(
            method = "onHandSwing(Lnet/minecraft/network/packet/c2s/play/HandSwingC2SPacket;)V",
            at = @At("TAIL")
    )
    public void pony_bookshelf_onHandSwing(HandSwingC2SPacket packet, CallbackInfo ci) {
        ServerPlayNetworkHandler handler = (ServerPlayNetworkHandler) (Object) this;
        ServerPlayerEntity player = handler.player;
        ItemStack itemStack = player.getStackInHand(packet.getHand());
        Item item = itemStack.getItem();

        if (item instanceof MeleeWeapon) {
            ((MeleeWeapon) item).onLeftClick(player, itemStack);
        }
    }

    @Inject(
            method = "onPlayerInteractEntity(Lnet/minecraft/network/packet/c2s/play/PlayerInteractEntityC2SPacket;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/network/ServerPlayerEntity;setSneaking(Z)V"
            ),
            cancellable = true
    )
    public void pony_bookshelf_onPlayerInteractEntity(PlayerInteractEntityC2SPacket packet, CallbackInfo ci) {
        ServerPlayNetworkHandler handler = (ServerPlayNetworkHandler) (Object) this;
        ServerPlayerEntity player = handler.player;
        ItemStack itemStack = player.getMainHandStack();
        Item item = itemStack.getItem();

        if (item instanceof BookItem) {
            ci.cancel();
        }
    }
}
