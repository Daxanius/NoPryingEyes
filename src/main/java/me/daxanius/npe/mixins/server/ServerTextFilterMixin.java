package me.daxanius.npe.mixins.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.server.network.ServerTextFilter;
import net.minecraft.server.network.FilteredText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

// Disables message filtering by passing all messages as permitted
@Mixin(ServerTextFilter.class)
public class ServerTextFilterMixin {

    /**
     * @reason Makes the AbstractTextFilterer class pass everything as filtered
     * so that profanity is allowed
     * @author Daxanius
     */
    @WrapOperation(at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;supplyAsync(Ljava/util/function/Supplier;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"), method = "requestMessageProcessing(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Lnet/minecraft/server/network/ServerTextFilter$IgnoreStrategy;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;")
    private CompletableFuture<Object> filterMessage(Supplier<Object> supplier, Executor executor, Operation<CompletableFuture<Object>> original, @Local(ordinal = 0, argsOnly = true) String raw) {
        if (NoPryingEyesConfig.getInstance().disable_chat_control) {
            NoPryingEyes.LogVerbose("Passing message as permitted");
            return CompletableFuture.completedFuture(FilteredText.passThrough(raw));
        }
        return original.call(supplier, executor);
    }
}