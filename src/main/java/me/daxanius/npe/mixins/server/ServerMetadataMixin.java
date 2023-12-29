package me.daxanius.npe.mixins.server;

import org.spongepowered.asm.mixin.Mixin;

import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.server.ServerMetadata;

@Mixin(ServerMetadata.class)
public class ServerMetadataMixin {
    private boolean preventsChatReports = NoPryingEyesConfig.getInstance().noKey();
}
