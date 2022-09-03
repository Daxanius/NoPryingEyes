package daxanius.npe.mixins.client;

import com.mojang.authlib.minecraft.UserApiService;
import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.telemetry.TelemetrySender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Environment(EnvType.CLIENT)
@Mixin(TelemetrySender.class)
public class TelemetryMixin {
	@Shadow
	private boolean sent;

	// This does not prevent collecting telemetry data, but it prevents
	// sending the collected telemetry data by telling
	// it that it has already sent the data
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/MinecraftClient;Lcom/mojang/authlib/minecraft/UserApiService;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/UUID;)V")
	private void init(MinecraftClient client, UserApiService userApiService, Optional<String> userId, Optional<String> clientId, UUID deviceSessionId, CallbackInfo info) {
		NoPryingEyes.LogVerbose("Minecraft is collecting telemetry data");
		sent = ConfigManager.getConfig().disable_telemetry;

		// Just to make sure nothing went wrong here
		NoPryingEyes.LogVerbose("Blocked telemetry data: " + sent);
	}
}