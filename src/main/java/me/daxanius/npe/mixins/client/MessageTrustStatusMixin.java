package me.daxanius.npe.mixins.client;

import me.daxanius.npe.config.ConfigManager;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.MessageVerifier;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.time.Instant;

@Mixin(MessageTrustStatus.class)
public class MessageTrustStatusMixin {
    // The original way of getting the status
    private static MessageTrustStatus getStatusOriginal(SignedMessage message, Text decorated, PlayerListEntry sender, Instant receptionTimestamp) {
        if (sender == null) {
            return MessageTrustStatus.NOT_SECURE;
        }

        MessageVerifier.Status status = sender.getMessageVerifier().verify(message);
        if (status == MessageVerifier.Status.BROKEN_CHAIN) {
            return MessageTrustStatus.BROKEN_CHAIN;
        }

        if (status == MessageVerifier.Status.NOT_SECURE) {
            return MessageTrustStatus.NOT_SECURE;
        }

        if (message.isExpiredOnClient(receptionTimestamp)) {
            return MessageTrustStatus.NOT_SECURE;
        }

        if (!message.filterMask().isPassThrough()) {
            return MessageTrustStatus.FILTERED;
        }

        if (message.unsignedContent().isPresent()) {
            return MessageTrustStatus.MODIFIED;
        }

        return !decorated.contains(message.getSignedContent().decorated()) ? MessageTrustStatus.MODIFIED : MessageTrustStatus.SECURE;
    }

    /**
     * @reason Removes the insecure message indicator
     * @author Daxanius
     */

    @Overwrite
    public static MessageTrustStatus getStatus(SignedMessage message, Text decorated, PlayerListEntry sender, Instant receptionTimestamp) {
        MessageTrustStatus status = getStatusOriginal(message, decorated, sender, receptionTimestamp);

        switch (status) {
            case NOT_SECURE -> {
                if (ConfigManager.getConfig().chat_indicator.hide_red) {
                    return MessageTrustStatus.SECURE;
                }
                return status;
            }
            case MODIFIED, FILTERED -> {
                if (ConfigManager.getConfig().chat_indicator.hide_yellow) {
                    return MessageTrustStatus.SECURE;
                }
                return status;
            }
            default -> {
                return status;
            }
        }
    }
}