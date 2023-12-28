package me.daxanius.npe.mixins.common;

import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.network.message.LastSeenMessageList;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;

@Mixin(ChatMessageC2SPacket.class)
public class ChatMessageC2SPacketMixin {
    @Final
    @Nullable
    @Mutable
    @Shadow
    private MessageSignatureData signature;

    /**
     * @reason Upon creation of the packet, sets the message signature as null
     * @author Daxanius
     */

    @Inject(method = "<init>(Ljava/lang/String;Ljava/time/Instant;JLnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/network/message/LastSeenMessageList$Acknowledgment;)V", at = @At("TAIL"))
    private void init(String string, Instant timestamp, long salt, MessageSignatureData signature, LastSeenMessageList.Acknowledgment acknowledgment, CallbackInfo ci) {
        NoPryingEyes.LogVerbose("Creating message packet");

        if (NoPryingEyesConfig.getInstance().noSign()) {
            NoPryingEyes.LogVerbose("Stripping packet signature");
            this.signature = null;
        }
    }

    /**
     * @reason Strips incoming messages of their signature so that it
     * won't accidentally land in the wrong hands
     * @author Daxanius
     */

    @Inject(method = "signature", at = @At("HEAD"), cancellable = true)
    private void signature(CallbackInfoReturnable<MessageSignatureData> info) {
        NoPryingEyes.LogVerbose("Requested signature from message packet");

        if (NoPryingEyesConfig.getInstance().noSign()) {
            NoPryingEyes.LogVerbose("Stripping packet signature");
            info.setReturnValue(null);
        }
    }
}