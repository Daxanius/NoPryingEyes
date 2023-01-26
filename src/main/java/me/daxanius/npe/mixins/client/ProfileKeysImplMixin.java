package me.daxanius.npe.mixins.client;

import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ProfileKeysImpl;
import net.minecraft.network.encryption.PlayerKeyPair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
@Mixin(ProfileKeysImpl.class)
public class ProfileKeysImplMixin {
    /**
     * @reason Prevents the client from accessing the key
     * @author Daxanius
     */

    @Inject(method = "fetchKeyPair()Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), cancellable = true)
    public void fetchKeyPair(CallbackInfoReturnable<Optional<PlayerKeyPair>> info) {
        NoPryingEyes.LogVerbose("Client is fetching profile public key");

        if (NoPryingEyesConfig.getInstance().noKey()) {
            NoPryingEyes.LogVerbose("Returning empty key");
            info.setReturnValue(Optional.empty());
            return;
        }

        NoPryingEyes.LogVerbose("Returning profile key");
    }

    /**
     * @reason Prevents the client from accessing the key pair from storage so it won't send it to the server
     * @author Daxanius
     */

    @Inject(method = "loadKeyPairFromFile()Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private void loadKeyPairFromFile(CallbackInfoReturnable<Optional<PlayerKeyPair>> info) {
        NoPryingEyes.LogVerbose("Client requested key pair from file");

        if (NoPryingEyesConfig.getInstance().noKey()) {
            NoPryingEyes.LogVerbose("Returning empty key pair");
            info.setReturnValue(Optional.empty());
            return;
        }

        NoPryingEyes.LogVerbose("Fetching key pair");
    }

    /**
     * @reason Prevents the client from generating a new key pair
     * @author Daxanius
     */

    @Inject(method = "getKeyPair(Ljava/util/Optional;)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), cancellable = true)
    private void getKeyPair(CallbackInfoReturnable<CompletableFuture<Optional<PlayerKeyPair>>> info) {
        NoPryingEyes.LogVerbose("Client requested key pair");

        if (NoPryingEyesConfig.getInstance().noKey()) {
            NoPryingEyes.LogVerbose("Returning empty key pair");
            info.setReturnValue(CompletableFuture.completedFuture(Optional.empty()));
            return;
        }

        NoPryingEyes.LogVerbose("Fetching key pair");
    }
}