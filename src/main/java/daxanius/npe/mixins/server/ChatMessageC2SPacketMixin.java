package daxanius.npe.mixins.server;

import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatMessageC2SPacket.class)
public class ChatMessageC2SPacketMixin {

    /**
     * @reason Strips incoming messages of their signature so that it
     * won't accidentally land in the wrong hands
     * @author Daxanius
     */

    @Inject(method = "signature", at = @At("HEAD"), cancellable = true)
    private void signature(CallbackInfoReturnable<MessageSignatureData> info) {
        NoPryingEyes.LogVerbose("Received message packet");

        if (ConfigManager.getConfig().noSign()) {
            NoPryingEyes.LogVerbose("Stripping message signature");
            info.setReturnValue(MessageSignatureData.EMPTY);
        }
    }
}