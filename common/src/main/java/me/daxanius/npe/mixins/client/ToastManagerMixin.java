package me.daxanius.npe.mixins.client;

import me.daxanius.npe.NoPryingEyesCommon;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ToastManager.class)
public class ToastManagerMixin {

    /**
     * @reason Allows the client to disable NPE toasts
     * @author Daxanius
     */

    @Inject(method = "addToast(Lnet/minecraft/client/gui/components/toasts/Toast;)V", at = @At("HEAD"), cancellable = true)
    public void add(Toast toast, CallbackInfo info) {
        if (NoPryingEyesConfig.getInstance().server_toasts) {
            return;
        }

        if (toast instanceof SystemToast t && t.getToken() == SystemToast.SystemToastId.UNSECURE_SERVER_WARNING) {
            NoPryingEyesCommon.logVerbose("Blocking system toast for server info");
            info.cancel();
        }
    }
}
