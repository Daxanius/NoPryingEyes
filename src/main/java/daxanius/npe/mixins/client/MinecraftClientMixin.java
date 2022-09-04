package daxanius.npe.mixins.client;

import com.mojang.authlib.minecraft.BanDetails;
import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Environment(EnvType.CLIENT)
@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    /**
     * @reason prevents the client from fetching valid ban details,
     * providing full clientside control, despite the player being banned.
     * @author Daxanius
     */

    // That's right, we grab it at it's root :-)
    @Inject(at = @At("HEAD"), method = "getMultiplayerBanDetails()Lcom/mojang/authlib/minecraft/BanDetails;", cancellable = true)
    private void getMultiplayerBanDetails(CallbackInfoReturnable<BanDetails> info) {
        NoPryingEyes.LogVerbose("Client is fetching ban details");

        // This mixin prioritizes a fake ban over the respect ms bans option, so if you enable
        // the fake ban setting, it will always display as banned
        if (ConfigManager.getConfig().fake_ban) {
            NoPryingEyes.LogVerbose("Falsifying ban details");
            info.setReturnValue(new BanDetails(new UUID(0, 0), null, "Fake Ban", "You have enabled the Fake Ban option"));
            return;
        }

        if (ConfigManager.getConfig().disable_global_bans) {
            NoPryingEyes.LogVerbose("Falsifying ban details");
            info.setReturnValue(null);
        }
    }
}