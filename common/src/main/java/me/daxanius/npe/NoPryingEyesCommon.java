package me.daxanius.npe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoPryingEyesCommon {
    public static final String MOD_ID = "nopryingeyes";
    public static final String MOD_NAME = "NoPryingEyes";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    public static final ThreadLocal<Boolean> shouldCloseToNPEDemandWarningScreen = ThreadLocal.withInitial(() -> false);

    public static void logVerbose(String s) {
        if (!NoPryingEyesConfig.getInstance().verbose) {
            return;
        }

        LOG.info(s);
    }
}
