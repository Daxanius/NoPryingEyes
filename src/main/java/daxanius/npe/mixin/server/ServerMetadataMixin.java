package daxanius.npe.mixin.server;

import daxanius.npe.config.ConfigManager;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerMetadata.class)
public class ServerMetadataMixin {
    // Allows clients who don't sign their messages to join
    @Inject(method = "isSecureChatEnforced()Z", at = @At("HEAD"), cancellable = true)
    public void isSecureChatEnforced(CallbackInfoReturnable<Boolean> info) {
        if (ConfigManager.getConfig().disable_message_signing) {
            info.setReturnValue(false);
        }
    }
}