package me.daxanius.npe.mixins.server;

import net.minecraft.network.message.DecoratedContents;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayNetworkHandler.class)
public interface DecoratedContentsInvoker {
    @Invoker("getDecoratedContents")
    DecoratedContents invokeGetDecoratedContents(ChatMessageC2SPacket packet);
}
