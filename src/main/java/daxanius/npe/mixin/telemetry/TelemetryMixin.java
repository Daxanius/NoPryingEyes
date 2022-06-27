package daxanius.npe.mixin.telemetry;

import com.mojang.authlib.minecraft.UserApiService;
import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.telemetry.TelemetrySender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.UUID;

@Mixin(TelemetrySender.class)
public class TelemetryMixin {
	// This does not prevent collecting telemetry data, but it prevents
	// sending the collected telemetry data by telling
	// it that it has already sent the data
	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/client/MinecraftClient;Lcom/mojang/authlib/minecraft/UserApiService;Ljava/util/Optional;Ljava/util/Optional;Ljava/util/UUID;)V")
	private void init(MinecraftClient client, UserApiService userApiService, Optional<String> userId, Optional<String> clientId, UUID deviceSessionId, CallbackInfo info) {
		NoPryingEyes.LOGGER.info("Minecraft is collecting telemetry data");
		((TelemetryAccessor) ((TelemetrySender)(Object)this)).setSent(!ConfigManager.getConfig().telemetry);

		// Just to make sure nothing went wrong here
		NoPryingEyes.LOGGER.info("Blocked telemetry data: " + ((TelemetryAccessor) (TelemetrySender)(Object)this).getSent());
	}
}