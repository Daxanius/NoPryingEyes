package me.daxanius.npe.mixins.server;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.filter.AbstractTextFilterer;
import net.minecraft.server.filter.FilteredMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

// Disables message filtering by passing all messages as permitted
@Mixin(AbstractTextFilterer.class)
public class AbstractTextFiltererMixin {

    /**
     * @reason Makes the AbstractTextFilterer class pass everything as filtered
     * so that profanity is allowed
     * @author Daxanius
     */

    @WrapOperation(at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;supplyAsync(Ljava/util/function/Supplier;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"), method = "filter(Lcom/mojang/authlib/GameProfile;Ljava/lang/String;Lnet/minecraft/server/filter/AbstractTextFilterer$HashIgnorer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;")
    private CompletableFuture<FilteredMessage> filterMessage(Supplier<Object> supplier, Executor executor, Operation<CompletableFuture<Object>> original, @Local(ordinal = 0, argsOnly = true) String raw) {
        return CompletableFuture.completedFuture(FilteredMessage.permitted(raw));
    }
}