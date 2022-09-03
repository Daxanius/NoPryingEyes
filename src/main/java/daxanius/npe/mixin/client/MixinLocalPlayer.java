package daxanius.npe.mixin.client;

import com.mojang.brigadier.ParseResults;
import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.network.message.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class MixinLocalPlayer {
    @Inject(method = "signChatMessage(Lnet/minecraft/network/message/MessageMetadata;Lnet/minecraft/network/message/DecoratedContents;Lnet/minecraft/network/message/LastSeenMessageList;)Lnet/minecraft/network/message/MessageSignatureData;", at = @At("HEAD"), cancellable = true)
    private void signChatMessage(MessageMetadata metadata, DecoratedContents content, LastSeenMessageList lastSeenMessages, CallbackInfoReturnable<MessageSignatureData> info) {
        if (ConfigManager.getConfig().disable_message_signing) {
            NoPryingEyes.LogVerbose("Signing message with empty signature");
            info.setReturnValue(MessageSignatureData.EMPTY);
            info.cancel();
            return;
        }

        NoPryingEyes.LogVerbose("Client is signing message");
    }

    @Inject(method = "signArguments(Lnet/minecraft/network/message/MessageMetadata;Lcom/mojang/brigadier/ParseResults;Lnet/minecraft/text/Text;Lnet/minecraft/network/message/LastSeenMessageList;)Lnet/minecraft/network/message/ArgumentSignatureDataMap;", at = @At("HEAD"), cancellable = true)
    private void signArguments(MessageMetadata signer, ParseResults<CommandSource> parseResults, @Nullable Text preview, LastSeenMessageList lastSeenMessages, CallbackInfoReturnable<ArgumentSignatureDataMap> info) {
        if (ConfigManager.getConfig().disable_message_signing) {
            NoPryingEyes.LogVerbose("Signing command args with empty signature");
            info.setReturnValue(ArgumentSignatureDataMap.EMPTY);
            info.cancel();
            return;
        }

        NoPryingEyes.LogVerbose("Client is signing command arguments");
    }
}