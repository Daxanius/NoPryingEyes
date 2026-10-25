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
import java.util.Optional;

@Mixin(ServerboundChatPacket.class)
public class ServerboundChatPacketMixin {
    @Final
    @Mutable
    @Shadow
    private Optional<MessageSignature> signature;

    /**
     * @reason Upon creation of the packet, sets the message signature as null
     * @author Daxanius
     */
    @Inject(method = "<init>(Ljava/lang/String;Ljava/time/Instant;JLjava/util/Optional;Lnet/minecraft/network/chat/LastSeenMessages$Update;)V", at = @At("TAIL"))
    private void init(String message, Instant timeStamp, long salt, Optional<MessageSignature> signature, LastSeenMessages.Update acknowledgment, CallbackInfo ci) {
        NoPryingEyesCommon.logVerbose("Creating message packet");

        if (NoPryingEyesConfig.getInstance().noSign()) {
            NoPryingEyesCommon.logVerbose("Stripping packet signature");
            this.signature = Optional.empty();
        }
    }

    /**
     * @reason Strips incoming messages of their signature so that it
     * won't accidentally land in the wrong hands
     * @author Daxanius
     */
    @Inject(method = "signature", at = @At("HEAD"), cancellable = true)
    private void signature(CallbackInfoReturnable<Optional<MessageSignature>> info) {
        NoPryingEyesCommon.logVerbose("Requested signature from message packet");

        if (NoPryingEyesConfig.getInstance().noSign()) {
            NoPryingEyesCommon.logVerbose("Stripping packet signature");
            info.setReturnValue(Optional.empty());
        }
    }
}
