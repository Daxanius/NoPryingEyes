package daxanius.npe.mixin.bannedgui;

import daxanius.npe.config.ConfigManager;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    @Inject(at = @At("HEAD"), method = "method_44692()Lnet/minecraft/Text;", cancellable = true)
    private void bannedCheck(CallbackInfoReturnable<Text> ci) {
        // if (!ConfigManager.getConfig().banned_gui) {
        //    ci.setReturnValue(null);
        //    ci.cancel();
        // }
    }
}