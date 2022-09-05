package daxanius.npe.mixins.server;

import daxanius.npe.config.ConfigManager;
import net.minecraft.server.dedicated.ServerPropertiesHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Properties;

@Mixin(ServerPropertiesHandler.class)
public class ServerPropertiesHandlerMixin {
    @Final
    @Mutable
    @Shadow
    public boolean enforceSecureProfile;

    /**
     * @reason Tells the server that secure profiles are not enabled
     * when keys are disabled in the NPE config settings
     * @author Daxanius
     */

    @Inject(method = "<init>(Ljava/util/Properties;)V", at = @At("TAIL"))
    private void init(Properties properties, CallbackInfo info) {
        // This way we still respect the config if noKey is disabled
        enforceSecureProfile = enforceSecureProfile && !ConfigManager.getConfig().noKey();
    }
}