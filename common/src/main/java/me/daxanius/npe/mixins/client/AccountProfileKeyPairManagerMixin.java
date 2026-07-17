package me.daxanius.npe.mixins.client;

import me.daxanius.npe.NoPryingEyesCommon;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.client.multiplayer.AccountProfileKeyPairManager;
import net.minecraft.world.entity.player.ProfileKeyPair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Mixin(AccountProfileKeyPairManager.class)
public class AccountProfileKeyPairManagerMixin {
    /**
     * @reason Prevents the client from accessing the key pair from storage so it won't send it to the server
     * @author Daxanius
     */
    @Inject(method = "readProfileKeyPair()Ljava/util/Optional;", at = @At("HEAD"), cancellable = true)
    private void loadKeyPairFromFile(CallbackInfoReturnable<Optional<ProfileKeyPair>> info) {
        NoPryingEyesCommon.logVerbose("Client requested key pair from file");

        if (NoPryingEyesConfig.getInstance().noKey()) {
            NoPryingEyesCommon.logVerbose("Returning empty key pair");
            info.setReturnValue(Optional.empty());
            return;
        }

        NoPryingEyesCommon.logVerbose("Fetching key pair");
    }

    /**
     * @reason Prevents the client from generating a new key pair
     * @author Daxanius
     */
    @Inject(method = "readOrFetchProfileKeyPair(Ljava/util/Optional;)Ljava/util/concurrent/CompletableFuture;", at = @At("HEAD"), cancellable = true)
    private void getKeyPair(CallbackInfoReturnable<CompletableFuture<Optional<ProfileKeyPair>>> info) {
        NoPryingEyesCommon.logVerbose("Client requested key pair");

        if (NoPryingEyesConfig.getInstance().noKey()) {
            NoPryingEyesCommon.logVerbose("Returning empty key pair");
            info.setReturnValue(CompletableFuture.completedFuture(Optional.empty()));
            return;
        }

        NoPryingEyesCommon.logVerbose("Fetching key pair");
    }
}
