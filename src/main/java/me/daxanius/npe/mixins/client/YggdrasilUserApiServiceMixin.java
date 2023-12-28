package me.daxanius.npe.mixins.client;

import java.util.concurrent.Executor;

import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.mojang.authlib.minecraft.TelemetrySession;
import com.mojang.authlib.yggdrasil.YggdrasilUserApiService;

@Mixin(value = YggdrasilUserApiService.class, remap = false)
public class YggdrasilUserApiServiceMixin {
    @Inject(method = "newTelemetrySession", at = @At("HEAD"), cancellable = true)
    private void onCreateTelemetrySession(Executor executor, CallbackInfoReturnable<TelemetrySession> info) {
        NoPryingEyes.LogVerbose("Yggdrasil is attempting to create a telemetry session");

        if (NoPryingEyesConfig.getInstance().disable_telemetry) {
            NoPryingEyes.LogVerbose("Disabling telemetry");
            info.setReturnValue(TelemetrySession.DISABLED);
        }

        NoPryingEyes.LogVerbose("Creating telemetry session");
    }
}