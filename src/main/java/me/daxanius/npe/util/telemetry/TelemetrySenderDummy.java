package me.daxanius.npe.util.telemetry;

import me.daxanius.npe.NoPryingEyes;
import net.minecraft.client.telemetry.TelemetryPropertyMap;
import net.minecraft.client.telemetry.TelemetryEventType;
import net.minecraft.client.telemetry.TelemetryEventSender;

import java.util.function.Consumer;

public class TelemetrySenderDummy implements TelemetryEventSender {
    @Override
    public void send(TelemetryEventType eventType, Consumer<TelemetryPropertyMap.Builder> propertyAdder) {
        // Just to make sure nothing went wrong here
        NoPryingEyes.LogVerbose("Blocked telemetry data: " + propertyAdder.toString());
    }
}