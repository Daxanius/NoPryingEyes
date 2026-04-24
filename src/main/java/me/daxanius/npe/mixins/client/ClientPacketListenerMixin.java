package me.daxanius.npe.mixins.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import me.daxanius.npe.mixins.client.ClientCommonPacketListenerImplAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.chat.ChatListener;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.daxanius.npe.gui.NoPryingEyesWarningScreens.*;

@Environment(EnvType.CLIENT)
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
            NoPryingEyes.LogVerbose("Opening warning toast.");
            SystemToast systemToast = SystemToast.multiline(this.client, SystemToast.SystemToastId.UNSECURE_SERVER_WARNING, UNSECURE_SERVER_TOAST_TITLE, SECURE_SERVER_TOAST_TEXT);
            this.client.getToastManager().addToast(systemToast);
            NoPryingEyesConfig.getInstance().setToastHasBeenSent(true);
        }
    }

    @WrapOperation(method = "handleSystemChat(Lnet/minecraft/network/protocol/game/ClientboundSystemChatPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/chat/ChatListener;handleSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"))
    private void showWarning(ChatListener instance, Component message, boolean overlay, Operation<Void> original) {
        // Yep, this is how we check it
        if (NoPryingEyesConfig.getInstance().noSign()) {
            if (isThisASignatureExceptionMessage(message)) {
                Minecraft.getInstance().setScreen(REQUIRED_MESSAGE_AND_COMMAND_SIGNING);
            } else if (messageCompareException(message, "chat.disabled.invalid_command_signature")) {
                Minecraft.getInstance().setScreen(REQUIRED_MESSAGE_SIGNING);
            }
        }
        original.call(instance, message, overlay);
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