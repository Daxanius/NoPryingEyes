package me.daxanius.npe.mixins.server;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.network.message.*;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {

    /**
     * @reason Stops clients from being able to report
     * received messages by removing signatures
     * @author Daxanius
     */

    @Inject(method = "getSignedMessage(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;)Lnet/minecraft/network/message/SignedMessage;", at = @At("HEAD"), cancellable = true)
    private void getSignedMessage(ChatMessageC2SPacket packet, CallbackInfoReturnable<SignedMessage> info) {
        if (NoPryingEyesConfig.getInstance().noSign()) {
            DecoratedContents contents = ((DecoratedContentsInvoker) this).invokeGetDecoratedContents(packet);
            info.setReturnValue(SignedMessage.ofUnsigned(contents));
        }
    }
}