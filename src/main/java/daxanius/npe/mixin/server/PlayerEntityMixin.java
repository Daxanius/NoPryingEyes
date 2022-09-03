package daxanius.npe.mixin.server;


import daxanius.npe.config.ConfigManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    // Makes sure that the public key is not sent to other clients
    @Inject(method = "getPublicKey()Lnet/minecraft/network/encryption/PlayerPublicKey;", at = @At("HEAD"), cancellable = true)
    private void onGetProfileKey(CallbackInfoReturnable<PlayerPublicKey> info) {
        if (ConfigManager.getConfig().disable_message_signing) {
            info.setReturnValue(null);
        }
    }
}