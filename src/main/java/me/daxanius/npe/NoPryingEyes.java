package me.daxanius.npe;

import me.daxanius.npe.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoPryingEyes implements ModInitializer {
	public static final String MODID = "npe";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static void LogVerbose(String s) {
		if (!ConfigManager.getConfig().verbose) {
			return;
		}

		LOGGER.info(s);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initializing config");
		ConfigManager.registerAutoConfig();
	}
}