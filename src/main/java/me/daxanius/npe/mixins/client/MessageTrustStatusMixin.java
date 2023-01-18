package me.daxanius.npe.mixins.client;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.MessageVerifier;
import net.minecraft.network.message.MessageVerifier.Impl;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import net.minecraft.server.PlayerManager;

import java.time.Instant;

@Mixin(MessageTrustStatus.class)
public class MessageTrustStatusMixin {
    // The original way of getting the status
    private static MessageTrustStatus getStatusOriginal(SignedMessage message, Text decorated, PlayerListEntry sender, Instant receptionTimestamp) {
        if (sender == null) {
            return MessageTrustStatus.NOT_SECURE;
        }

       MessageTrustStatus status = sender.getMessageVerifier().verify(message);
        if (status == MessageTrustStatus.BROKEN_CHAIN) {
            return MessageTrustStatus.BROKEN_CHAIN;
        }

        if (status == MessageTrustStatus.NOT_SECURE) {
            return MessageTrustStatus.NOT_SECURE;
        }

        if (message.isExpiredOnClient(receptionTimestamp)) {
            return MessageTrustStatus.NOT_SECURE;
        }

        if (!message.filterMask().isPassThrough()) {
            return MessageTrustStatus.MODIFIED;
        }

        if (message.unsignedContent().isPresent()) {
            return MessageTrustStatus.MODIFIED;
        }

        return !decorated.contains(message.getContent()) ? MessageTrustStatus.MODIFIED : MessageTrustStatus.SECURE;
    }

    /**
     * @reason Removes the insecure message indicator
     * @author Daxanius
     */

    @Overwrite
    public static MessageTrustStatus getStatus(SignedMessage message, Text decorated, Instant receptionTimestamp) {
        MessageTrustStatus status = getStatusOriginal(message, decorated, null, receptionTimestamp);

        switch (status) {
            case NOT_SECURE -> {
                if (NoPryingEyesConfig.getInstance().chat_indicator.hide_red) {
                    return MessageTrustStatus.SECURE;
                }
                return status;
            }
            case MODIFIED -> {
                if (NoPryingEyesConfig.getInstance().chat_indicator.hide_yellow) {
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