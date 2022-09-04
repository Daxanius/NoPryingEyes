package daxanius.npe.mixins.client;

import com.mojang.brigadier.ParseResults;
import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.network.message.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    /**
     * @reason Signs a message empty before sending it to the server,
     * that way the server won't receive the player's key
     * @author Daxanius
     */

    // Sign messages empty before sending them
    @Inject(method = "signChatMessage(Lnet/minecraft/network/message/MessageMetadata;Lnet/minecraft/network/message/DecoratedContents;Lnet/minecraft/network/message/LastSeenMessageList;)Lnet/minecraft/network/message/MessageSignatureData;", at = @At("HEAD"), cancellable = true)
    private void signChatMessage(MessageMetadata metadata, DecoratedContents content, LastSeenMessageList lastSeenMessages, CallbackInfoReturnable<MessageSignatureData> info) {
        if (ConfigManager.getConfig().disable_message_signing) {
            NoPryingEyes.LogVerbose("Signing message with empty signature");
            info.setReturnValue(MessageSignatureData.EMPTY);
            return;
        }

        NoPryingEyes.LogVerbose("Client is signing message");
    }

    /**
     * @reason Commands are also signed by default, this
     * is also disabled here
     * @author Daxanius
     */

    // Sign commands empty before sending them
    @Inject(method = "signArguments(Lnet/minecraft/network/message/MessageMetadata;Lcom/mojang/brigadier/ParseResults;Lnet/minecraft/text/Text;Lnet/minecraft/network/message/LastSeenMessageList;)Lnet/minecraft/network/message/ArgumentSignatureDataMap;", at = @At("HEAD"), cancellable = true)
    private void signArguments(MessageMetadata signer, ParseResults<CommandSource> parseResults, @Nullable Text preview, LastSeenMessageList lastSeenMessages, CallbackInfoReturnable<ArgumentSignatureDataMap> info) {
        if (ConfigManager.getConfig().disable_message_signing) {
            NoPryingEyes.LogVerbose("Signing command args with empty signature");
            info.setReturnValue(ArgumentSignatureDataMap.EMPTY);
            return;
        }

        NoPryingEyes.LogVerbose("Client is signing command arguments");
    }
}