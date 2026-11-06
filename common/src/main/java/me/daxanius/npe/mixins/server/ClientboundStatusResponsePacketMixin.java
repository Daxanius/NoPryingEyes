package me.daxanius.npe.mixins.server;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientboundStatusResponsePacket.class)
public class ClientboundStatusResponsePacketMixin {
    /**
     * @reason The status response is encoded through a StreamCodec built from ServerStatus.CODEC,
     * not through FriendlyByteBuf#writeJsonWithCodec, so the codec itself is wrapped to add the
     * "preventsChatReports" field to the server list ping
     */
    @ModifyArg(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/codec/ByteBufCodecs;fromCodec(Lcom/mojang/serialization/DynamicOps;Lcom/mojang/serialization/Codec;)Lnet/minecraft/network/codec/StreamCodec$CodecOperation;"),
            index = 1
    )
    private static Codec<ServerStatus> wrapStatusCodec(Codec<ServerStatus> codec) {
        Encoder<ServerStatus> encoder = new Encoder<>() {
            @Override
            public <T> DataResult<T> encode(ServerStatus input, DynamicOps<T> ops, T prefix) {
                return codec.encode(input, ops, prefix).flatMap(encoded -> ops.mergeToMap(
                        encoded,
                        ops.createString("preventsChatReports"),
                        ops.createBoolean(NoPryingEyesConfig.getInstance().noSign())
                ));
            }
        };

        return Codec.of(encoder, codec, "NoPryingEyes[" + codec + "]");
    }
}
