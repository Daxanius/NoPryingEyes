package me.daxanius.npe.mixin;

import me.daxanius.npe.NoPryingEyesCommon;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class MixinTitleScreen {

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {

        NoPryingEyesCommon.LOG.info("This line is printed by an example mod mixin from NeoForge!");
        NoPryingEyesCommon.LOG.info("MC Version: {}", SharedConstants.getCurrentVersion().name());
    }
}