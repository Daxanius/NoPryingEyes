package me.daxanius.npe.mixins.client;

import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import me.daxanius.npe.util.telemetry.TelemetrySenderDummy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.telemetry.ClientTelemetryManager;
import net.minecraft.client.telemetry.TelemetryEventSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientTelemetryManager.class)
public class ClientTelemetryManagerMixin {
	/**
	 * @reason Provide sa dummy telemetry sender that does not send data
	 * @author Daxanius
	 */

	@Inject(method = "getOutsideSessionSender()Lnet/minecraft/client/telemetry/TelemetryEventSender;", at = @At("HEAD"), cancellable = true)
	private void getSender(CallbackInfoReturnable<TelemetryEventSender> info) {
		NoPryingEyes.LogVerbose("Minecraft is requesting the telemetry sender");

		if (NoPryingEyesConfig.getInstance().disable_telemetry) {
			NoPryingEyes.LogVerbose("Creating dummy telemetry sender");
			info.setReturnValue(new TelemetrySenderDummy());
            return;
		}

		NoPryingEyes.LogVerbose("Creating telemetry sender");
	}
}