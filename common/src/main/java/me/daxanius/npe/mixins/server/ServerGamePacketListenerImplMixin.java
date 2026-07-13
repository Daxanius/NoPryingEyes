package me.daxanius.npe.mixins.server;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.SignedMessageBody;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {

    /**
     * @reason Stops clients from being able to report
     * received messages by sending them as system messages
     * @author Daxanius
     */
    @Inject(method = "getSignedMessage(Lnet/minecraft/network/protocol/game/ServerboundChatPacket;Lnet/minecraft/network/chat/LastSeenMessages;)Lnet/minecraft/network/chat/PlayerChatMessage;", at = @At("HEAD"), cancellable = true)
    private void getSignedMessage(ServerboundChatPacket packet, LastSeenMessages lastSeenMessages, CallbackInfoReturnable<PlayerChatMessage> info) {
        SignedMessageBody messageBody = new SignedMessageBody(packet.message(), packet.timeStamp(), packet.salt(), lastSeenMessages);

        if (NoPryingEyesConfig.getInstance().noSign()) {
            info.setReturnValue(PlayerChatMessage.system(messageBody.content()));
        }
    }
}
