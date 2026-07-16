package me.daxanius.npe.mixins.client;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.client.multiplayer.chat.ChatTrustLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;

import java.time.Instant;
import java.util.Optional;

@Mixin(ChatTrustLevel.class)
public class ChatTrustLevelMixin {
    // The original way of getting the status
    @Unique
    private static ChatTrustLevel npe$getStatusOriginal(PlayerChatMessage message, Component decorated, Instant receptionTimestamp) {
        if (message.hasSignature() && !message.hasExpiredClient(receptionTimestamp)) {
            return npe$isModified(message, decorated) ? ChatTrustLevel.MODIFIED : ChatTrustLevel.SECURE;
        } else {
            return ChatTrustLevel.NOT_SECURE;
        }
    }

    @Unique
    private static boolean npe$isModified(PlayerChatMessage message, Component decorated) {
        if (!decorated.getString().contains(message.signedContent())) {
            return true;
        } else {
            Component text = message.unsignedContent();
            return text != null && npe$isNotInDefaultFont(text);
        }
    }

    @Unique
    private static boolean npe$isNotInDefaultFont(Component content) {
        return content.visit((style, part) -> npe$isNotInDefaultFont(style) ? Optional.of(true) : Optional.empty(), Style.EMPTY).orElse(false);
    }

    @Unique
    private static boolean npe$isNotInDefaultFont(Style style) {
        return !style.getFont().equals(FontDescription.DEFAULT);
    }

    /**
     * @reason Removes the insecure message indicator
     * @author Daxanius
     */
    @Overwrite
    public static ChatTrustLevel evaluate(PlayerChatMessage message, Component decorated, Instant receptionTimestamp) {
        ChatTrustLevel status = npe$getStatusOriginal(message, decorated, receptionTimestamp);

        switch (status) {
            case NOT_SECURE -> {
                if (NoPryingEyesConfig.getInstance().chat_indicator.hide_red) {
                    return ChatTrustLevel.SECURE;
                }
                return status;
            }
            case MODIFIED -> {
                if (NoPryingEyesConfig.getInstance().chat_indicator.hide_yellow) {
                    return ChatTrustLevel.SECURE;
                }
                return status;
            }
            default -> {
                return status;
            }
        }
    }
}
