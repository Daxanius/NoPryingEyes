package daxanius.npe.mixins.server;

import daxanius.npe.config.ConfigManager;
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
    @Inject(method = "isSecureChatEnforced()Z", at = @At("HEAD"), cancellable = true)
    public void isSecureChatEnforced(CallbackInfoReturnable<Boolean> info) {
        if (ConfigManager.getConfig().noKey()) {
            info.setReturnValue(false);
        }
    }
}