package me.daxanius.npe.config;

import me.daxanius.npe.NoPryingEyes;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = NoPryingEyes.MODID)
public class NoPryingEyesConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    public boolean disable_telemetry = true;

    @ConfigEntry.Gui.Tooltip()
    public boolean disable_global_bans = true;

    @ConfigEntry.Gui.Tooltip()
    public boolean disable_profanity_filter = true;

    @ConfigEntry.Gui.Tooltip()
    public boolean server_toasts = true;

    @ConfigEntry.Gui.Tooltip()
    public boolean fake_ban = false;

    @ConfigEntry.Gui.Tooltip()
    public boolean verbose = false;

    @ConfigEntry.Gui.Tooltip()
    @ConfigEntry.Gui.EnumHandler()
    public SigningMode signing_mode = SigningMode.NO_KEY;

    // For ease of use
    public boolean noSign() {
        return signing_mode != SigningMode.SIGN;
    }

    public boolean noKey() {
        return signing_mode == SigningMode.NO_KEY;
    }

    // There are 3 signing modes which the user can select
    public enum SigningMode {
        // Sign messages normally
        SIGN,

        // Don't sign messages, but still send the public key
        NO_SIGN,

        // Don't send the public key at all
        NO_KEY
    }
}