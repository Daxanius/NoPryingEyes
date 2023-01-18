package me.daxanius.npe.mixins.client;

import com.mojang.authlib.minecraft.UserApiService;
import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.telemetry.TelemetryManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Environment(EnvType.CLIENT)
@Mixin(TelemetryManager.class)
public class TelemetryMixin {
	@Shadow
	private boolean sent;

	/**
	 * @reason Make the game think it has already sent its
	 * telemetry data, since a neat boolean has already been created for us
	 * @author Daxanius
	 */

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/MinecraftClient;Lcom/mojang/authlib/minecraft/UserApiService;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/UUID;)V")
	private void init(MinecraftClient client, UserApiService userApiService, Optional<String> userId, Optional<String> clientId, UUID deviceSessionId, CallbackInfo info) {
		NoPryingEyes.LogVerbose("Minecraft is collecting telemetry data");
		sent = NoPryingEyesConfig.getInstance().disable_telemetry;

		// Just to make sure nothing went wrong here
		NoPryingEyes.LogVerbose("Blocked telemetry data: " + sent);
	}
}