package daxanius.npe.mixin.unsigner;

import net.minecraft.network.message.*;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.filter.FilteredMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

// NOTE: Credits to https://github.com/fxmorin/Unsigner/
// I did not write this code, I only modified it to suit my needs
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
    @Overwrite
    private void handleMessage(ChatMessageC2SPacket packet, FilteredMessage<String> message) {
        MessageDecorator messageDecorator = this.server.getMessageDecorator();
        messageDecorator.decorateChat(
                this.player,
                message.map(Text::literal),
                MessageSignature.none(),
                false
        ).thenAcceptAsync(this::broadcastUnsignedChatMessage, this.server);
    }

    private void broadcastUnsignedChatMessage(FilteredMessage<SignedMessage> filtered) {
        for (ServerPlayerEntity player : this.server.getPlayerManager().getPlayerList()) {
            player.sendMessage(
                    Text.empty()
                            .append("<")
                            .append(player.getDisplayName())
                            .append("> ")
                            .append(filtered.raw().getContent()),
                    MessageType.SYSTEM
            );
        }
        this.checkForSpam();
    }
}