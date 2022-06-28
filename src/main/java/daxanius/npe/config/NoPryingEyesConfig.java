package daxanius.npe.config;

import daxanius.npe.NoPryingEyes;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = NoPryingEyes.MODID)
public class NoPryingEyesConfig implements ConfigData {
    @Comment("Send telemetry data to Mojang")
    public boolean telemetry = false;

    // Temporarily disabled until mappings are available
    // @Comment("Disable some UI elements if you are banned from Minecraft")
    // public boolean banned_gui = false;
}