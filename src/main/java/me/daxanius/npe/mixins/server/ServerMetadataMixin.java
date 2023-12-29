package me.daxanius.npe.mixins.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.server.ServerMetadata;

@Mixin(ServerMetadata.class)
public abstract class ServerMetadataMixin {
    @Unique
    public boolean preventsChatReports = NoPryingEyesConfig.getInstance().noKey();
}
