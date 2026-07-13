package me.daxanius.npe;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.daxanius.npe.config.ClothConfigScreenFactory;
import net.fabricmc.loader.api.FabricLoader;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        // TODO: fix broken config screen warning when cloth config is not installed
        if (!FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return null;
        }

        return ClothConfigScreenFactory::build;
    }
}
