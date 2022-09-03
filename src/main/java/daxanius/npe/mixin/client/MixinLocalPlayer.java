package daxanius.npe.mixin.client;

import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.message.DecoratedContents;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.message.MessageMetadata;
import net.minecraft.network.message.MessageSignatureData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class MixinLocalPlayer {
    @Inject(method = "signChatMessage(Lnet/minecraft/network/message/MessageMetadata;Lnet/minecraft/network/message/DecoratedContents;Lnet/minecraft/network/message/LastSeenMessageList;)Lnet/minecraft/network/message/MessageSignatureData;", at = @At("HEAD"), cancellable = true)
    private void signChatMessage(MessageMetadata metadata, DecoratedContents content, LastSeenMessageList lastSeenMessages, CallbackInfoReturnable<MessageSignatureData> info) {
        if (ConfigManager.getConfig().disable_message_signing) {
            NoPryingEyes.LogVerbose("Signing message with empty signature");
            info.setReturnValue(MessageSignatureData.EMPTY);
            info.cancel();
            return;
        }

        NoPryingEyes.LogVerbose("Client is signing message");
    }
}