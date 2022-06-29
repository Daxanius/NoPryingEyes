package daxanius.npe.mixin.telemetry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.telemetry.TelemetrySender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(TelemetrySender.class)
public interface TelemetryAccessor {
    // Allows us to manipulate the TelemetrySender object to think it has
    // already sent it's data
    @Accessor("sent")
    void setSent(boolean sent);

    @Accessor
    boolean getSent();
}