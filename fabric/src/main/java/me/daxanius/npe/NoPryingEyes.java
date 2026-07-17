package me.daxanius.npe;

import net.fabricmc.api.ModInitializer;

public class NoPryingEyes implements ModInitializer {

    @Override
    public void onInitialize() {
        NoPryingEyesCommon.initialize();
    }
}
