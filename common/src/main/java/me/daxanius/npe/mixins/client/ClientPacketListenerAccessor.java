package me.daxanius.npe.mixins.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientPacketListener.class)
public interface ClientPacketListenerAccessor {
    @Accessor("serverEnforcesSecureChat")
    boolean getServerEnforcesSecureChat();
}
