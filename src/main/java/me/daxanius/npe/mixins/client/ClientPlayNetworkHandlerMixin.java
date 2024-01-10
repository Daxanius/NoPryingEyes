package me.daxanius.npe.mixins.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.toast.SystemToast;
import net.minecraft.network.packet.s2c.play.ServerMetadataS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.daxanius.npe.NoPryingEyes;
import me.daxanius.npe.config.NoPryingEyesConfig;
import me.daxanius.npe.gui.NoPryingEyesWarningScreen;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin {
    @Final
    @Shadow
    private static final Text UNSECURE_SERVER_TOAST_TITLE = Text.translatable("npe.title");

    @Final
    @Shadow
    private static final Text UNSECURE_SERVER_TOAST_TEXT = Text.translatable("npe.modified_chat.toast");

    private MinecraftClient client = ((ClientCommonNetworkHandlerAccessor)this).getClient();

    ServerInfo serverInfo = ((ClientPlayNetworkHandler)(Object)this).getServerInfo();

    
    /**
     * @reason Common method to intercept messages/commands from client
     * @author TechPro424
     */
    

    private void interceptMessages() {
        Text warning = Text.translatable("npe.warning.on_demand");
        NoPryingEyes.LogVerbose("Setting warn screen (does not work right now)");
        MinecraftClient.getInstance().setScreen(new NoPryingEyesWarningScreen(warning));
        NoPryingEyes.LogVerbose("Sending warn message");
        client.player.sendMessage(warning);
        NoPryingEyes.LogVerbose("Enabling sign for 1 session");
        NoPryingEyesConfig.getInstance().setTempSign(true);
    }

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


    /**
     * @reason injects to intercept messages/commands from client and show warning scrren
     * @author TechPro424
     */
    
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void sendChatMessage(String content, CallbackInfo ci) {

        if(serverInfo != null) if (serverInfo.isSecureChatEnforced() && NoPryingEyesConfig.getInstance().onDemand() && !NoPryingEyesConfig.getInstance().tempSign()) interceptMessages();
        
    }

    @Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
    private void sendChatCommand(String content, CallbackInfo ci) {

        if(serverInfo != null) if (serverInfo.isSecureChatEnforced() && NoPryingEyesConfig.getInstance().onDemand() && !NoPryingEyesConfig.getInstance().tempSign()) interceptMessages();
        
    }

    @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    private void sendCommand(String content, CallbackInfoReturnable<Boolean> cir) {

        if(serverInfo != null) if (serverInfo.isSecureChatEnforced() && NoPryingEyesConfig.getInstance().onDemand() && !NoPryingEyesConfig.getInstance().tempSign()) interceptMessages();
        
    }
    
}