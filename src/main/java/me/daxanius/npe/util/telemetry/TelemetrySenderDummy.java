package me.daxanius.npe.util.telemetry;

import me.daxanius.npe.NoPryingEyes;
import net.minecraft.client.util.telemetry.PropertyMap;
import net.minecraft.client.util.telemetry.TelemetryEventType;
import net.minecraft.client.util.telemetry.TelemetrySender;

import java.util.function.Consumer;

public class TelemetrySenderDummy implements TelemetrySender {
    @Override
    public void send(TelemetryEventType eventType, Consumer<PropertyMap.Builder> propertyAdder) {
        // Just to make sure nothing went wrong here
        NoPryingEyes.LogVerbose("Blocked telemetry data: " + propertyAdder.toString());
    }
}