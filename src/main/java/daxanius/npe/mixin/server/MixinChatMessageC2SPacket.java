package daxanius.npe.mixin.server;

import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatMessageC2SPacket.class)
public class MixinChatMessageC2SPacket {
    // Remove signature of received message
    @Inject(method = "signature", at = @At("HEAD"), cancellable = true)
    private void signature(CallbackInfoReturnable<MessageSignatureData> info) {
        NoPryingEyes.LogVerbose("Received message packet");

        if (ConfigManager.getConfig().disable_message_signing) {
            NoPryingEyes.LogVerbose("Stripping message signature");
            info.setReturnValue(MessageSignatureData.EMPTY);
        }
    }
}