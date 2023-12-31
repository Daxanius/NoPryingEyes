package me.daxanius.npe.mixins.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.network.packet.s2c.play.ServerMetadataS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Final
    @Shadow
    private static final Text UNSECURE_SERVER_TOAST_TITLE = Text.translatable("npe.title");

    @Final
    @Shadow
    private static final Text UNSECURE_SERVER_TOAST_TEXT = Text.translatable("npe.modified_chat.toast");

    private MinecraftClient client;



    /**
     * @reason Add a warning message
     * @author Daxanius
     */

    @Inject(method = "onServerMetadata(Lnet/minecraft/network/packet/s2c/play/ServerMetadataS2CPacket;)V", at = @At("TAIL"))
    private void onServerMetaData(ServerMetadataS2CPacket packet, CallbackInfo info) {
        if (packet.isSecureChatEnforced()) {
            SystemToast systemToast = SystemToast.create(this.client, SystemToast.Type.UNSECURE_SERVER_WARNING, UNSECURE_SERVER_TOAST_TITLE, Text.translatable("npe.unmodified_chat.toast"));
            this.client.getToastManager().add(systemToast);
        }
    }
}