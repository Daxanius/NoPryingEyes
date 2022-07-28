package daxanius.npe.config;

import daxanius.npe.NoPryingEyes;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = NoPryingEyes.MODID)
public class NoPryingEyesConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    public boolean telemetry = false;

    @ConfigEntry.Gui.Tooltip()
    public boolean respect_ms_bans = false;

    @ConfigEntry.Gui.Tooltip()
    public boolean reports = false;

    // This setting is enabled by default since it can diminish
    // vanilla compatibility with servers and clients
    @ConfigEntry.Gui.Tooltip()
    public boolean message_signing = true;

    @ConfigEntry.Gui.Tooltip()
    public boolean profanity_filter = false;

    @ConfigEntry.Gui.Tooltip()
    public boolean fake_ban = false;

    @ConfigEntry.Gui.Tooltip()
    public boolean verbose = false;
}