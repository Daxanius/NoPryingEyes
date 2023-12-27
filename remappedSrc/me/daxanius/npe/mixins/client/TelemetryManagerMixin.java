package me.daxanius.npe.mixins.client;

import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import me.daxanius.npe.util.telemetry.TelemetrySenderDummy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.session.telemetry.TelemetryManager;
import net.minecraft.client.session.telemetry.TelemetrySender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(TelemetryManager.class)
public class TelemetryManagerMixin {
	/**
	 * @reason Provide sa dummy telemetry sender that does not send data
	 * @author Daxanius
	 */

	@Inject(method = "getSender()Lnet/minecraft/client/util/telemetry/TelemetrySender;", at = @At("HEAD"), cancellable = true)
	private void getSender(CallbackInfoReturnable<TelemetrySender> info) {
		NoPryingEyes.LogVerbose("Minecraft is requesting the telemetry sender");

		if (NoPryingEyesConfig.getInstance().disable_telemetry) {
			NoPryingEyes.LogVerbose("Creating dummy telemetry sender");
			info.setReturnValue(new TelemetrySenderDummy());
		}

		NoPryingEyes.LogVerbose("Creating telemetry sender");
	}
}