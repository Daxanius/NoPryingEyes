package daxanius.npe.config;

import daxanius.npe.NoPryingEyes;
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
    public boolean disable_message_signing = true;

    @ConfigEntry.Gui.Tooltip()
    public boolean disable_profanity_filter = true;

    @ConfigEntry.Gui.Tooltip()
    public boolean fake_ban = false;

    @ConfigEntry.Gui.Tooltip()
    public boolean verbose = false;
}