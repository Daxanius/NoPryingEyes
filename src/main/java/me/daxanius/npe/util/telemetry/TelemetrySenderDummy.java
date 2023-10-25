package me.daxanius.npe.util.telemetry;

import me.daxanius.npe.NoPryingEyes;
import net.minecraft.client.session.telemetry.PropertyMap;
import net.minecraft.client.session.telemetry.TelemetryEventType;
import net.minecraft.client.session.telemetry.TelemetrySender;

import java.util.function.Consumer;

public class TelemetrySenderDummy implements TelemetrySender {
    @Override
    public void send(TelemetryEventType eventType, Consumer<PropertyMap.Builder> propertyAdder) {
        // Just to make sure nothing went wrong here
        NoPryingEyes.LogVerbose("Blocked telemetry data: " + propertyAdder.toString());
    }
}