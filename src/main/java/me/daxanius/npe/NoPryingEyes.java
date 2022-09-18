package me.daxanius.npe;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoPryingEyes implements ModInitializer {
	public static final String MODID = "npe";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

	public static void LogVerbose(String s) {
		if (!NoPryingEyesConfig.getInstance().verbose) {
			return;
		}

		LOGGER.info(s);
	}

	@Override
	public void onInitialize() {
		NoPryingEyesConfig.loadConfig();
	}
}