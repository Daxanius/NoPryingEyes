package daxanius.npe.mixin.server;

import daxanius.npe.NoPryingEyes;
import daxanius.npe.config.ConfigManager;
import net.minecraft.network.message.*;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.filter.FilteredMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// NOTE: Credits to https://github.com/fxmorin/Unsigner/
// I did not write this code, I only modified it to suit my needs
// Disables reporting by sending player messages as broadcasts instead of signed messages
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow
    @Final
    private MinecraftServer server;

    @Shadow
    public ServerPlayerEntity player;

    @Shadow
    protected abstract void checkForSpam();

    /**
     * @author FX - PR0CESS
     * @reason stop reporting
     */
    @Inject(at = @At("HEAD"), method = "handleMessage(Lnet/minecraft/network/packet/c2s/play/ChatMessageC2SPacket;Lnet/minecraft/server/filter/FilteredMessage;)V", cancellable = true)
    private void handleMessage(ChatMessageC2SPacket packet, FilteredMessage<String> message, CallbackInfo info) {
        NoPryingEyes.LogVerbose("Player message received");
        if (!ConfigManager.getConfig().reports) {
            NoPryingEyes.LogVerbose("Broadcasting received message to prevent reporting");
            MessageDecorator messageDecorator = this.server.getMessageDecorator();
            messageDecorator.decorateFiltered(
                    this.player,
                    message.map(Text::literal)
            ).thenAcceptAsync(this::broadcastUnsignedChatMessage, this.server);
            info.cancel();
            return;
        }

        NoPryingEyes.LogVerbose("Sending message normally, allowing for player reports");
    }

    private void broadcastUnsignedChatMessage(FilteredMessage<Text> filtered) {
        Text message = Text.empty()
                .append("<")
                .append(this.player.getDisplayName())
                .append("> ")
                .append(filtered.raw());

        // Log the modified message to the console
        NoPryingEyes.LOGGER.info("[UNSIGNED CHAT] {}", message.getString());

        // Forward the message to each player on the server
        for (ServerPlayerEntity player : this.server.getPlayerManager().getPlayerList()) {
            player.sendMessage(message, false);
        }

        this.checkForSpam();
    }
}