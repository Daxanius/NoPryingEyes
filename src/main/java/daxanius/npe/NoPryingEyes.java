package daxanius.npe;

import daxanius.npe.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoPryingEyes implements ModInitializer {
	public static final String MODID = "npe";
	public static int[] SEMVER;

	public static Identifier identifier(String path) {
		return new Identifier(MODID, path);
	}

	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initializing config");
		ConfigManager.registerAutoConfig();
	}
}
