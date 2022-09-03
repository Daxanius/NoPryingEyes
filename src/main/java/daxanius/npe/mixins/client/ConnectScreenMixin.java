package daxanius.npe.mixins.client;

import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import daxanius.npe.gui.NoPryingEyesWarningScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ConnectScreen.class)
public class ConnectScreenMixin {
    // Shows a warning screen and returns when the server does not allow unsigned messages
    @Inject(method = "connect(Lnet/minecraft/client/gui/screen/Screen;Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;)V", at = @At("HEAD"), cancellable = true)
    private static void connect(Screen screen, MinecraftClient client, ServerAddress address, @Nullable ServerInfo info, CallbackInfo callbackInfo) {
        NoPryingEyes.LogVerbose("Checking server info");

        if (info == null) {
            NoPryingEyes.LOGGER.error("Could not determine server info");
            callbackInfo.cancel();
            return;
        }

        if (info.isSecureChatEnforced() && ConfigManager.getConfig().disable_message_signing) {
            NoPryingEyes.LogVerbose("Server enforces signed messages, aborting");
            client.setScreen(new NoPryingEyesWarningScreen(Text.translatable("npe.warning.server_signing")));
            callbackInfo.cancel();
        }
    }
}