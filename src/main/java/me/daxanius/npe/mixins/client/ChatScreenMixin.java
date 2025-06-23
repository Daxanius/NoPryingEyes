package me.daxanius.npe.mixins.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
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
    @ModifyExpressionValue(method = "sendMessage(Ljava/lang/String;Z)V", at = @At(value = "INVOKE", target = "Ljava/lang/String;isEmpty()Z"))
    private boolean interceptMessage(boolean original) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (!original
                && player != null
                && ((ClientPlayNetworkHandlerAccessor) player.networkHandler).getSecureChatEnforced()
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

    @ModifyArg(method = "keyPressed(III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 1))
    private Screen redirectExitScreen(@Nullable Screen screen) {
        if (shouldCloseToNPEDemandWarningScreen.get()) {
            shouldCloseToNPEDemandWarningScreen.set(false);
            NoPryingEyes.LogVerbose("Opening on demand warn screen");
            return ON_DEMAND_SIGNING_ENABLED;
        }
        return screen;
    }
}
