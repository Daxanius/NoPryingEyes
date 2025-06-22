package me.daxanius.npe.mixins.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static me.daxanius.npe.gui.NoPryingEyesWarningScreens.*;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Final
    @Shadow
    private static final Text UNSECURE_SERVER_TOAST_TITLE = Text.translatable("npe.title");

    @Final
    @Shadow
    private static final Text UNSECURE_SERVER_TOAST_TEXT = Text.translatable("npe.modified_chat.toast");

    @Unique
    private final MinecraftClient client = ((ClientCommonNetworkHandlerAccessor) this).getClient();

    /**
     * @reason Add a warning message
     * @author Daxanius
     */

    @Inject(method = "onGameJoin(Lnet/minecraft/network/packet/s2c/play/GameJoinS2CPacket;)V", at = @At("TAIL"))
    private void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        if (packet.enforcesSecureChat()) {
            SystemToast systemToast = SystemToast.create(this.client, SystemToast.Type.UNSECURE_SERVER_WARNING, UNSECURE_SERVER_TOAST_TITLE, UNSECURE_SERVER_TOAST_TEXT);
            this.client.getToastManager().add(systemToast);
            NoPryingEyesConfig.getInstance().setToastHasBeenSent(true);
        }
    }

    @WrapOperation(method = "onGameMessage(Lnet/minecraft/network/packet/s2c/play/GameMessageS2CPacket;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/message/MessageHandler;onGameMessage(Lnet/minecraft/text/Text;Z)V"))
    private void showWarning(MessageHandler instance, Text message, boolean overlay, Operation<Void> original) {
        // Yep, this is how we check it
        if (NoPryingEyesConfig.getInstance().noKey()) {
            if (isThisASignatureExceptionMessage(message)) {
                MinecraftClient.getInstance().setScreen(REQUIRED_MESSAGE_AND_COMMAND_SIGNING);
            } else if (messageCompareException(message, "chat.disabled.invalid_command_signature")) {
                MinecraftClient.getInstance().setScreen(REQUIRED_MESSAGE_SIGNING);
            }
        }
        original.call(instance, message, overlay);
    }

    @Unique
    private boolean isThisASignatureExceptionMessage(Text message) {
        return messageCompareException(message, "chat.disabled.missingProfileKey") ||
                messageCompareException(message, "chat.disabled.expiredProfileKey") ||
                messageCompareException(message, "chat.disabled.invalid_signature");
    }

    @Unique
    private boolean messageCompareException(Text message, String key) {
        return message.equals(Text.translatable(key).formatted(Formatting.RED));
    }

}