package me.daxanius.npe.mixins.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.daxanius.npe.NoPryingEyesCommon;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.daxanius.npe.gui.NoPryingEyesWarningScreens.REQUIRED_MESSAGE_AND_COMMAND_SIGNING;
import static me.daxanius.npe.gui.NoPryingEyesWarningScreens.REQUIRED_MESSAGE_SIGNING;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Final
    @Shadow
    private static final Component UNSECURE_SERVER_TOAST_TITLE = Component.translatable("npe.title");

    @Final
    @Shadow
    private static final Component UNSERURE_SERVER_TOAST = Component.translatable("npe.modified_chat.toast");

    @Unique
    private static final Component SECURE_SERVER_TOAST_TEXT = Component.translatable("npe.unmodified_chat.toast");

    @Unique
    private final Minecraft client = ((ClientCommonPacketListenerImplAccessor) this).getMinecraft();

    /**
     * @reason Add a warning message
     * @author Daxanius
     */
    @Inject(method = "handleLogin(Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;)V", at = @At("TAIL"))
    private void onGameJoin(ClientboundLoginPacket packet, CallbackInfo info) {
        if (packet.enforcesSecureChat()) {
            NoPryingEyesCommon.logVerbose("Opening warning toast.");
            SystemToast systemToast = new SystemToast(SystemToast.SystemToastId.UNSECURE_SERVER_WARNING, UNSECURE_SERVER_TOAST_TITLE, SECURE_SERVER_TOAST_TEXT);
            this.client.gui.toastManager().addToast(systemToast);
            NoPryingEyesConfig.getInstance().setToastHasBeenSent(true);
        }
    }

    @WrapOperation(method = "handleSystemChat(Lnet/minecraft/network/protocol/game/ClientboundSystemChatPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handleSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void showWarning(ChatListener instance, Component message, boolean remote, Operation<Void> original) {
        // Yep, this is how we check it
        if (NoPryingEyesConfig.getInstance().noSign()) {
            if (isThisASignatureExceptionMessage(message)) {
                Minecraft.getInstance().gui.setScreen(REQUIRED_MESSAGE_AND_COMMAND_SIGNING);
            } else if (messageCompareException(message, "chat.disabled.invalid_command_signature")) {
                Minecraft.getInstance().gui.setScreen(REQUIRED_MESSAGE_SIGNING);
            }
        }
        original.call(instance, message, remote);
    }

    @Unique
    private boolean isThisASignatureExceptionMessage(Component message) {
        return messageCompareException(message, "chat.disabled.missingProfileKey") ||
                messageCompareException(message, "chat.disabled.expiredProfileKey") ||
                messageCompareException(message, "chat.disabled.invalid_signature");
    }

    @Unique
    private boolean messageCompareException(Component message, String key) {
        return message.equals(Component.translatable(key).withStyle(ChatFormatting.RED));
    }
}
