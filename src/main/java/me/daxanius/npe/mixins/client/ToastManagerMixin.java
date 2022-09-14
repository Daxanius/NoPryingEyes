package me.daxanius.npe.mixins.client;

import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.client.toast.Toast;
import net.minecraft.client.toast.ToastManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ToastManager.class)
public class ToastManagerMixin {

    /**
     * @reason Allows the client to disable NPE toasts
     * @author Daxanius
     */

    @Inject(method = "Lnet/minecraft/client/toast/ToastManager;add(Lnet/minecraft/client/toast/Toast;)V", at = @At("HEAD"), cancellable = true)
    public void add(Toast toast, CallbackInfo info) {
        if (!ConfigManager.getConfig().server_toasts) {
            if (toast instanceof SystemToast t && t.getType() == SystemToast.Type.UNSECURE_SERVER_WARNING) {
                NoPryingEyes.LogVerbose("Blocking system toast for server info");
                info.cancel();
            }
        }
    }
}