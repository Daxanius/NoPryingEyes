package me.daxanius.npe.mixins.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import static me.daxanius.npe.NoPryingEyes.shouldCloseToNPEDemandWarningScreen;
import static me.daxanius.npe.gui.NoPryingEyesWarningScreens.ON_DEMAND_SIGNING_ENABLED;

@Mixin(ChatScreen.class)
public class ChatScreenMixin {
    /**
     * @reason injects to intercept messages/commands from client and show warning screen
     * @author TechPro424
     */
    @ModifyExpressionValue(method = "handleChatInput(Ljava/lang/String;Z)V", at = @At(value = "INVOKE", target = "Ljava/lang/String;isEmpty()Z"))
    private boolean interceptMessage(boolean original) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (!original
                && player != null
                && ((ClientPacketListenerAccessor) player.connection).getServerEnforcesSecureChat()
                && NoPryingEyesConfig.getInstance().onDemand()
                && !NoPryingEyesConfig.getInstance().tempSign()) {
            NoPryingEyes.LogVerbose("Enabling sign for 1 session");
            NoPryingEyesConfig.getInstance().setTempSign(true);
            NoPryingEyesConfig.OnDemandWarning on_demand_warning = NoPryingEyesConfig.getInstance().onDemandWarning;
            if (on_demand_warning == NoPryingEyesConfig.OnDemandWarning.ALWAYS
                    || (on_demand_warning == NoPryingEyesConfig.OnDemandWarning.IF_TOAST_NOT_SENT && !NoPryingEyesConfig.getInstance().toastHasBeenSent())) {
                shouldCloseToNPEDemandWarningScreen.set(true);
            }
            return true;
        }
        return original;

    }

    @ModifyArg(method = "keyPressed(Lnet/minecraft/client/input/KeyEvent;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    private Screen redirectExitScreen(@Nullable Screen screen) {
        if (shouldCloseToNPEDemandWarningScreen.get()) {
            shouldCloseToNPEDemandWarningScreen.set(false);
            NoPryingEyes.LogVerbose("Opening on demand warn screen");
            return ON_DEMAND_SIGNING_ENABLED;
        }
        return screen;
    }
}
