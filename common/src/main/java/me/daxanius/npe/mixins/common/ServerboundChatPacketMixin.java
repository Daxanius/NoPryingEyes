package me.daxanius.npe.mixins.common;

import me.daxanius.npe.NoPryingEyesCommon;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.network.chat.LastSeenMessages;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
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

@Mixin(ServerboundChatPacket.class)
public class ServerboundChatPacketMixin {
    @Final
    @Nullable
    @Mutable
    @Shadow
    private MessageSignature signature;

    /**
     * @reason Upon creation of the packet, sets the message signature as null
     * @author Daxanius
     */
    @Inject(method = "<init>(Ljava/lang/String;Ljava/time/Instant;JLnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/network/chat/LastSeenMessages$Update;)V", at = @At("TAIL"))
    private void init(String string, Instant timestamp, long salt, MessageSignature signature, LastSeenMessages.Update acknowledgment, CallbackInfo ci) {
        NoPryingEyesCommon.logVerbose("Creating message packet");

        if (NoPryingEyesConfig.getInstance().noSign()) {
            NoPryingEyesCommon.logVerbose("Stripping packet signature");
            this.signature = null;
        }
    }

    /**
     * @reason Strips incoming messages of their signature so that it
     * won't accidentally land in the wrong hands
     * @author Daxanius
     */
    @Inject(method = "signature", at = @At("HEAD"), cancellable = true)
    private void signature(CallbackInfoReturnable<MessageSignature> info) {
        NoPryingEyesCommon.logVerbose("Requested signature from message packet");

        if (NoPryingEyesConfig.getInstance().noSign()) {
            NoPryingEyesCommon.logVerbose("Stripping packet signature");
            info.setReturnValue(null);
        }
    }
}
