package me.daxanius.npe.mixins.server;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    /**
     * @reason Allows clients who don't send their public key
     * to join the server
     * @author Daxanius
     */

    // Allows clients who don't sign their messages to join
    @Inject(method = "shouldEnforceSecureProfile()Z", at = @At("HEAD"), cancellable = true)
    public void shouldEnforceSecureProfile(CallbackInfoReturnable<Boolean> info) {
        if (NoPryingEyesConfig.getInstance().noKey()) {
            info.setReturnValue(false);
        }
    }
}