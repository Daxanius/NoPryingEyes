package me.daxanius.npe;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoPryingEyes implements ModInitializer {
	public static final String MOD_ID = "npe";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static void LogVerbose(String s) {
		if (!NoPryingEyesConfig.getInstance().verbose) {
			return;
		}

		LOGGER.info(s);
	}

	@Override
	public void onInitialize() {
		NoPryingEyesConfig.load();
	}
}