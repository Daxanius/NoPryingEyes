package me.daxanius.npe;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(NoPryingEyesCommon.MOD_ID)
public class NoPryingEyes {

    public NoPryingEyes(IEventBus eventBus) {

        // This method is invoked by the NeoForge mod loader when it is ready
        // to load your mod. You can access NeoForge and Common code in this
        // project.

        // Use NeoForge to bootstrap the Common mod.
        NoPryingEyesCommon.LOG.info("Hello NeoForge world!");
        CommonClass.init();

    }
}