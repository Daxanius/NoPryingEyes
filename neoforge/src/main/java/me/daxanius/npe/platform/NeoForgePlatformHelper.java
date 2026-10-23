package me.daxanius.npe.platform;

import me.daxanius.npe.platform.services.IPlatformHelper;
import net.neoforged.fml.loading.FMLConfig;
import net.neoforged.fml.loading.FMLEnvironment;

import java.nio.file.Path;

public class NeoForgePlatformHelper implements IPlatformHelper {
    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLEnvironment.production;
    }

    @Override
    public Path getConfigDir() {
        return Path.of(FMLConfig.defaultConfigPath());
    }
}