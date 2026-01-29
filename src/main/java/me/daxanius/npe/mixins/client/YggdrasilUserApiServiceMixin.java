package me.daxanius.npe.mixins.client;

import java.util.UUID;
import java.util.concurrent.Executor;

import com.mojang.authlib.minecraft.UserApiService;
import com.mojang.authlib.minecraft.report.AbuseReportLimits;
import com.mojang.authlib.yggdrasil.request.AbuseReportRequest;
import com.mojang.authlib.yggdrasil.response.KeyPairResponse;
import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import me.daxanius.npe.util.telemetry.AbuseReportLimitsCustom;
import net.fabricmc.loader.api.FabricLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
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

    @Inject(method = "getKeyPair()Lcom/mojang/authlib/yggdrasil/response/KeyPairResponse;", at = @At("HEAD"), cancellable = true)
    public void onGetKeyPair(CallbackInfoReturnable<KeyPairResponse> info) {
        if (NoPryingEyesConfig.getInstance().noKey()) {
            info.setReturnValue(null);
        }
    }

    @Inject(method = "isBlockedPlayer(Ljava/util/UUID;)Z", at = @At("HEAD"), cancellable = true)
    public void onIsBlockedPlayer(final UUID playerID, CallbackInfoReturnable<Boolean> info) {
        if (NoPryingEyesConfig.getInstance().disable_global_bans) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "reportAbuse(Lcom/mojang/authlib/yggdrasil/request/AbuseReportRequest;)V", at = @At("HEAD"), cancellable = true)
    public void onReportAbuse(final AbuseReportRequest request, CallbackInfo info) {
        // Just completely ignore any attempted report requests
        info.cancel();
    }

    @Inject(method = "canSendReports()Z", at = @At("HEAD"), cancellable = true)
    private void onCanSendReports(CallbackInfoReturnable<Boolean> info) {
        // Only allow the reporting button in the devenv for testing purposes
        info.setReturnValue(FabricLoader.getInstance().isDevelopmentEnvironment());
    }

    @Inject(method = "getAbuseReportLimits()Lcom/mojang/authlib/minecraft/report/AbuseReportLimits;", at = @At("HEAD"), cancellable = true)
    public void onGetAbuseReportLimits(CallbackInfoReturnable<AbuseReportLimits> info) {
        info.setReturnValue(AbuseReportLimitsCustom.ZERO);
    }

    @Inject(method = "fetchProperties()Lcom/mojang/authlib/minecraft/UserApiService$UserProperties;", at = @At("HEAD"), cancellable = true)
    public void onFetchProperties(CallbackInfoReturnable<UserApiService.UserProperties> info) {
        info.setReturnValue(UserApiService.OFFLINE_PROPERTIES);
    }
}