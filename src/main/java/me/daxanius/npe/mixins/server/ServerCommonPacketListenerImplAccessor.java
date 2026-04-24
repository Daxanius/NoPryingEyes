package me.daxanius.npe.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;

@Mixin(ServerCommonPacketListenerImpl.class)
public interface ServerCommonPacketListenerImplAccessor {
    @Accessor("server")
    MinecraftServer getServer();
}
