package daxanius.npe.mixin.client;

import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// Bypasses Minecraft Clienside ban restrictions by falsifying the fetched ban data
@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    // That's right, we grab it at it's core :-)
    @Inject(at = @At("HEAD"), method = "getMultiplayerBanDetails()Lcom/mojang/authlib/minecraft/BanDetails;", cancellable = true)
    private void getMultiplayerBanDetails(CallbackInfoReturnable info) {
        NoPryingEyes.LogVerbose("Client is fetching ban details");

        if (!ConfigManager.getConfig().respect_ms_bans) {
            NoPryingEyes.LogVerbose("Falsifying ban details");
            info.setReturnValue(null);
            info.cancel();
            return;
        }
    }
}