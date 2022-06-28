package daxanius.npe.config;

import daxanius.npe.NoPryingEyes;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = NoPryingEyes.MODID)
public class NoPryingEyesConfig implements ConfigData {
    @ConfigEntry.Gui.Tooltip()
    public boolean telemetry = false;

    // @Comment("Respect Microsoft account bans")
    // @ConfigEntry.Gui.Tooltip()
    // public boolean respect_ms_bans = false;
    
    @ConfigEntry.Gui.Tooltip()
    public boolean reports = false;
}