package me.daxanius.npe.config;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public class ConfigManager {
    private static ConfigHolder<NoPryingEyesConfig> holder;

    public static void registerAutoConfig() {
        if (holder != null) {
            throw new IllegalStateException("Configuration already registered");
        }

        holder = AutoConfig.register(NoPryingEyesConfig.class, JanksonConfigSerializer::new);
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