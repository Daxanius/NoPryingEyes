package me.daxanius.npe.mixins.common;

import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.network.message.ArgumentSignatureDataMap;
import net.minecraft.network.message.MessageSignatureData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ArgumentSignatureDataMap.class)
public abstract class ArgumentSignatureDataMapMixin {
    @Final
    @Nullable
    @Mutable
    @Shadow
    private List<ArgumentSignatureDataMap.Entry> entries;


    @Shadow public abstract List<ArgumentSignatureDataMap.Entry> entries();

    /**
     * @reason Upon creation of the packet, sets the message signature as null
     * @author Daxanius
     */

    @Inject(method = "<init>(Ljava/util/List;)V", at = @At("TAIL"))
    private void init(List<ArgumentSignatureDataMap.Entry> list, CallbackInfo ci) {
        NoPryingEyes.LogVerbose("Creating message packet");

        if (NoPryingEyesConfig.getInstance().noSign() && entries != null) {
            NoPryingEyes.LogVerbose("Stripping packet signature");
            entries.clear();
        }
    }

    /**
     * @reason Strips incoming commands of their signature so that it
     * won't accidentally land in the wrong hands
     * @author Daxanius
     */

    @Inject(method = "get(Ljava/lang/String;)Lnet/minecraft/network/message/MessageSignatureData;", at = @At("HEAD"), cancellable = true)
    public void get(String argumentName, CallbackInfoReturnable<MessageSignatureData> info) {
        NoPryingEyes.LogVerbose("Requested signature from message packet");

        if (NoPryingEyesConfig.getInstance().noSign()) {
            NoPryingEyes.LogVerbose("Stripping packet signature");
            info.setReturnValue(null);
        }
    }

    /**
     * @reason Strips incoming messages of their signature so that it
     * won't accidentally land in the wrong hands
     * @author Daxanius
     */

    @Inject(method = "entries()Ljava/util/List;", at = @At("HEAD"))
    public void entries(CallbackInfoReturnable<List<ArgumentSignatureDataMap.Entry>> info) {
        NoPryingEyes.LogVerbose("Requested signature from message packet");

        if (NoPryingEyesConfig.getInstance().noSign() && entries != null) {
            NoPryingEyes.LogVerbose("Stripping packet signature");
            entries.clear();
        }
    }
}
