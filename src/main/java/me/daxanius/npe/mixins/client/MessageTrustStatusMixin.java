package me.daxanius.npe.mixins.client;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.client.network.message.MessageTrustStatus;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.time.Instant;
import java.util.Optional;

@Mixin(MessageTrustStatus.class)
public class MessageTrustStatusMixin {
    // The original way of getting the status
    @Unique
    private static MessageTrustStatus getStatusOriginal(SignedMessage message, Text decorated, Instant receptionTimestamp) {
        if (message.hasSignature() && !message.isExpiredOnClient(receptionTimestamp)) {
            return isModified(message, decorated) ? MessageTrustStatus.MODIFIED : MessageTrustStatus.SECURE;
        } else {
            return MessageTrustStatus.NOT_SECURE;
        }
    }

    @Unique
    private static boolean isModified(SignedMessage message, Text decorated) {
        if (!decorated.getString().contains(message.getSignedContent())) {
            return true;
        } else {
            Text text = message.unsignedContent();
            return text != null && isNotInDefaultFont(text);
        }
    }

    @Unique
    private static boolean isNotInDefaultFont(Text content) {
        return content.visit((style, part) -> isNotInDefaultFont(style) ? Optional.of(true) : Optional.empty(), Style.EMPTY).orElse(false);
    }

    @Unique
    private static boolean isNotInDefaultFont(Style style) {
        return !style.getFont().equals(Style.DEFAULT_FONT_ID);
    }

    /**
     * @reason Removes the insecure message indicator
     * @author Daxanius
     */

    @Overwrite
    public static MessageTrustStatus getStatus(SignedMessage message, Text decorated, Instant receptionTimestamp) {
        MessageTrustStatus status = getStatusOriginal(message, decorated, receptionTimestamp);

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