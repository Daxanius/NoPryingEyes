package me.daxanius.npe.mixins.server;


import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {

    /**
     * @reason Makes sure that the public key
     * is not sent to other clients
     * @author Daxanius
     */

    @Inject(method = "getPublicKey()Lnet/minecraft/network/encryption/PlayerPublicKey;", at = @At("HEAD"), cancellable = true)
    private void getPublicKey(CallbackInfoReturnable<PlayerPublicKey> info) {
        // This is set to no sign because the public key must never be forwarded to other clients
        // despite the possibility of NO_KEY not being set
        // It would be BAD to send the key to other clients
        if (NoPryingEyesConfig.getInstance().noSign()) {
            info.setReturnValue(null);
        }
    }
}