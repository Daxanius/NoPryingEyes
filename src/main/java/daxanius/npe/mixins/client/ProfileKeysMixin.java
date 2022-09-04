package daxanius.npe.mixins.client;

import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.ProfileKeys;
import net.minecraft.network.encryption.PlayerKeyPair;
import net.minecraft.network.encryption.PlayerPublicKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Environment(EnvType.CLIENT)
@Mixin(ProfileKeys.class)
public class ProfileKeysMixin {
    /**
     * @reason Prevents the client from accessing the key
     * @author Daxanius
     */

    @Inject(method = "getPublicKey()Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    public void getPublicKey(CallbackInfoReturnable<Optional<PlayerPublicKey>> info) {
        NoPryingEyes.LogVerbose("Client is fetching profile public key");

        if (ConfigManager.getConfig().disable_message_signing) {
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

        if (ConfigManager.getConfig().disable_message_signing) {
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

        if (ConfigManager.getConfig().disable_message_signing) {
            NoPryingEyes.LogVerbose("Returning empty key pair");
            info.setReturnValue(CompletableFuture.completedFuture(Optional.empty()));
            return;
        }

        NoPryingEyes.LogVerbose("Fetching key pair");
    }

    /**
     * @reason Prevents the client from getting the key by refreshing
     * @author Daxanius
     */

    @Inject(method = "refresh()Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), cancellable = true)
    public void refresh(CallbackInfoReturnable<CompletableFuture<Optional<PlayerPublicKey.PublicKeyData>>> info) {
        NoPryingEyes.LogVerbose("Client requested key data refresh");

        if (ConfigManager.getConfig().disable_message_signing) {
            NoPryingEyes.LogVerbose("Returning empty key data");
            info.setReturnValue(CompletableFuture.completedFuture(Optional.empty()));
            return;
        }

        NoPryingEyes.LogVerbose("Fetching key pair");
    }
}