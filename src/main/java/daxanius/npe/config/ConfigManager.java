package daxanius.npe.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

import java.util.function.Consumer;

public class ConfigManager {
    private static ConfigHolder<NoPryingEyesConfig> holder;
    public static final Consumer<NoPryingEyesConfig> DEFAULT = (i) -> {
        i.telemetry = false;
        // i.respect_ms_bans = false;
        i.reports = false;
        i.verbose = false;
    };

    public static void registerAutoConfig() {
        if (holder != null) {
            throw new IllegalStateException("Configuration already registered");
        }

        holder = AutoConfig.register(NoPryingEyesConfig.class, JanksonConfigSerializer::new);
        if (!getConfig().telemetry && !getConfig().reports && !getConfig().verbose) DEFAULT.accept(holder.getConfig());
        holder.save();
    }

    public static NoPryingEyesConfig getConfig() {
        if (holder == null) {
            return new NoPryingEyesConfig();
        }

        return holder.getConfig();
    }

    public static void load() {
        if (holder == null) {
            registerAutoConfig();
        }

        holder.load();
    }

    public static void save() {
        if (holder == null) {
            registerAutoConfig();
        }

        holder.save();
    }
}
