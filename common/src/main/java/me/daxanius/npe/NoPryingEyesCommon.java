package me.daxanius.npe;

import me.daxanius.npe.config.NoPryingEyesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoPryingEyesCommon {
    public static final String MOD_ID = "nopryingeyes";
    public static final String MOD_NAME = "NoPryingEyes";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static void logVerbose(String s) {
        if (!NoPryingEyesConfig.getInstance().verbose) {
            return;
        }

        LOG.info(s);
    }

    public static void initialize() {
        NoPryingEyesConfig.load();
    }
}
