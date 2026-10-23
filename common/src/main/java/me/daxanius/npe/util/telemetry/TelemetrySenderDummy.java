package me.daxanius.npe.util.telemetry;

import me.daxanius.npe.NoPryingEyesCommon;
import net.minecraft.client.telemetry.TelemetryEventSender;
import net.minecraft.client.telemetry.TelemetryEventType;
import net.minecraft.client.telemetry.TelemetryPropertyMap;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class TelemetrySenderDummy implements TelemetryEventSender {
    @Override
    public void send(@NotNull TelemetryEventType eventType, Consumer<TelemetryPropertyMap.Builder> propertyAdder) {
        NoPryingEyesCommon.logVerbose("Blocked telemetry data: " + propertyAdder.toString());
    }
}