package daxanius.npe.mixin.filter;

import com.mojang.authlib.GameProfile;
import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.minecraft.server.filter.FilteredMessage;
import net.minecraft.server.filter.TextFilterer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.Executor;

@Mixin(TextFilterer.class)
public class TextFiltererMixin {
    @Inject(at = @At("HEAD"), method = "filterMessage(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Lnet/minecraft/server/filter/TextFilterer$HashIgnorer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", cancellable = true)
    private void filterMessage(GameProfile gameProfile, String message, TextFilterer.HashIgnorer ignorer, Executor executor, CallbackInfoReturnable info) {
        NoPryingEyes.LogVerbose("Message queued to be checked for profanity");

        if (!ConfigManager.getConfig().profanity_filter) {
            NoPryingEyes.LogVerbose("Passing message as permitted");
            info.setReturnValue(FilteredMessage.permitted(message));
            info.cancel();
            return;
        }

        NoPryingEyes.LogVerbose("Checking for profanity");
    }
}