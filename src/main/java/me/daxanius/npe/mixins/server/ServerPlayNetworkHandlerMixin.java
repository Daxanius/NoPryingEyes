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
     * received messages by sending them as system messages
     * @author Daxanius
     */

    @Inject(method = "getSignedMessage(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;Lnet/minecraft/network/message/LastSeenMessageList;)Lnet/minecraft/network/message/SignedMessage;", at = @At("HEAD"), cancellable = true)
    private void getSignedMessage(ChatMessageC2SPacket packet, LastSeenMessageList lastSeenMessages, CallbackInfoReturnable<SignedMessage> info) {
        MessageBody messageBody = new MessageBody(packet.chatMessage(), packet.timestamp(), packet.salt(), lastSeenMessages);

        if (NoPryingEyesConfig.getInstance().noSign()) {
            info.setReturnValue(SignedMessage.ofUnsigned(messageBody.content()));
        }
    }
}