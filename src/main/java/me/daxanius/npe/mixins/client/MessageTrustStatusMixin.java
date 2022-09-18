package me.daxanius.npe.mixins.client;

import me.daxanius.npe.config.ConfigManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.time.Instant;

@Mixin(MessageTrustStatus.class)
public class MessageTrustStatusMixin {

    /**
     * @reason Removes the insecure message indicator
     * @author Daxanius
     */

    @Inject(method = "getStatus(Lnet/minecraft/network/message/SignedMessage;Lnet/minecraft/text/Text;Lnet/minecraft/client/network/PlayerListEntry;Ljava/time/Instant;)Lnet/minecraft/client/network/message/MessageTrustStatus;", at = @At("HEAD"), cancellable = true)
    private static void getStatus(SignedMessage message, Text decorated, PlayerListEntry sender, Instant receptionTimestamp, CallbackInfoReturnable<MessageTrustStatus> info) {
        MessageTrustStatus status = MessageTrustStatus.getStatus(message, decorated, sender, receptionTimestamp);

        switch (status) {
            case NOT_SECURE -> {
                if (ConfigManager.getConfig().chat_indicator.hide_red) {
                    info.setReturnValue(MessageTrustStatus.SECURE);
                    break;
                }
                info.setReturnValue(status);
            }
            case MODIFIED, FILTERED -> {
                if (ConfigManager.getConfig().chat_indicator.hide_yellow) {
                    info.setReturnValue(MessageTrustStatus.SECURE);
                    break;
                }
                info.setReturnValue(status);
            }
            default -> info.setReturnValue(status);
        }
    }
}