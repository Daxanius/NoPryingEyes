package me.daxanius.npe.mixins.server;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerMetadata.class)
public class ServerMetadataMixin {

    /**
     * @reason Allows clients who don't send their public key
     * to join the server
     * @author Daxanius
     */

    // Allows clients who don't sign their messages to join
    @Inject(method = "secureChatEnforced()Z", at = @At("HEAD"), cancellable = true)
    public void isSecureChatEnforced(CallbackInfoReturnable<Boolean> info) {
        if (NoPryingEyesConfig.getInstance().noKey()) {
            info.setReturnValue(false);
        }
    }
}