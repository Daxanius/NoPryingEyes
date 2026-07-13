package me.daxanius.npe;

import me.daxanius.npe.config.ClothConfigScreenFactory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = NoPryingEyesCommon.MOD_ID, dist = Dist.CLIENT)
public class NeoForgeClientSetup {
    public NeoForgeClientSetup(ModContainer container) {
        if (ModList.get().isLoaded("cloth_config")) {
            container.registerExtensionPoint(IConfigScreenFactory.class,
                    (_, parent) -> ClothConfigScreenFactory.build(parent));
        }
    }
}
