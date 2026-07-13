package me.daxanius.npe;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(NoPryingEyesCommon.MOD_ID)
public class NoPryingEyes {
    public NoPryingEyes(IEventBus eventBus) {
        NoPryingEyesCommon.initialize();
    }
}
