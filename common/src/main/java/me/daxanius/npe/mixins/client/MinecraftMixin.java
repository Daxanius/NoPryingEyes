package me.daxanius.npe.mixins.client;

import com.mojang.authlib.minecraft.BanDetails;
import me.daxanius.npe.NoPryingEyesCommon;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    /**
     * @reason prevents the client from fetching valid ban details,
     * providing full clientside control, despite the player being banned.
     * @author Daxanius
     */
    @Inject(method = "multiplayerBan()Lcom/mojang/authlib/minecraft/BanDetails;", at = @At("HEAD"), cancellable = true)
    private void getMultiplayerBanDetails(CallbackInfoReturnable<BanDetails> info) {
        NoPryingEyesCommon.logVerbose("Client is fetching ban details");

        // This mixin prioritizes a fake ban over the respect ms bans option, so if you enable
        // the fake ban setting, it will always display as banned
        if (NoPryingEyesConfig.getInstance().fake_ban) {
            NoPryingEyesCommon.logVerbose("Falsifying ban details");
            info.setReturnValue(new BanDetails(new UUID(0, 0), null, "Fake Ban", "You have enabled the Fake Ban option"));
            return;
        }

        if (NoPryingEyesConfig.getInstance().disable_global_bans) {
            NoPryingEyesCommon.logVerbose("Falsifying ban details");
            info.setReturnValue(null);
        }
    }

    @Inject(method = "isNameBanned()Z", at = @At("HEAD"), cancellable = true)
    public void isUsernameBanned(CallbackInfoReturnable<Boolean> info) {
        if (NoPryingEyesConfig.getInstance().disable_global_bans) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "clearDownloadedResourcePacks", at = @At("HEAD"))
    private void onDisconnected(CallbackInfo ci) {
        NoPryingEyesConfig.getInstance().setTempSign(false);
        NoPryingEyesConfig.getInstance().setToastHasBeenSent(false);
    }
}
