package daxanius.npe.mixins.client;

import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import daxanius.npe.gui.NoPryingEyesWarningScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(DisconnectedScreen.class)
public class DisconnectedScreenMixin {
    @Final
    @Shadow
    private Text reason;

    /**
     * @reason Display a custom warning screen when the server requires
     * message signing.
     * @author Daxanius
     */

    @Inject(method = "init()V", at = @At("HEAD"), cancellable = true)
    protected void init(CallbackInfo info) {
        String reason = this.reason.toString();
        NoPryingEyes.LogVerbose("Disconnect info: " + reason);

        // Yep, this is how we check it
        if (ConfigManager.getConfig().noSign()) {
            if (reason.contains("multiplayer.disconnect.missing_public_key")) {
                MinecraftClient.getInstance().setScreen(new NoPryingEyesWarningScreen(Text.translatable("npe.warning.server_key")));
                info.cancel();
                return;
            }

            if (reason.contains("multiplayer.disconnect.unsigned_chat")) {
                MinecraftClient.getInstance().setScreen(new NoPryingEyesWarningScreen(Text.translatable("npe.warning.server_signing")));
                info.cancel();
                // return;
            }
        }
    }
}