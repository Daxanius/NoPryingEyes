package me.daxanius.npe.mixins.common;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.At;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.ServerMetadata;
import net.minecraft.util.Util;

import io.netty.handler.codec.EncoderException;
import me.daxanius.npe.config.NoPryingEyesConfig;


@Mixin(PacketByteBuf.class)
public abstract class PacketByteBufMixin {
    @Shadow @Final
	private static Gson GSON;

    @Shadow
    public abstract PacketByteBuf writeString(String string);

    @Inject(method = "encodeAsJson", at = @At("HEAD"), cancellable = true)
    private void writeJson(Codec codec, Object value, CallbackInfo ci) {
        /*
         * The raw type and unchecked warnings are not an issue here
         * 
         * Raw type is needed because if Codec is parameterized with Object in the method,
         * The if statement throws an "Invalid operand types Object and ServerMetadata" error
         * 
         * The unchecked conversion warning is thrown because we have not parameterized Codec
         * But we are using an if statement to check the types anyway, so no issues
         */

        if (codec == ServerMetadata.CODEC) {
            ci.cancel();

            DataResult<JsonElement> dataResult = codec.encodeStart(JsonOps.INSTANCE, value);
            JsonElement element = Util.getResult(dataResult, string -> new EncoderException("Failed to encode: " + string + " " + value));

            element.getAsJsonObject().addProperty("preventsChatReports", NoPryingEyesConfig.getInstance().noKey());

            this.writeString(GSON.toJson(element));

        }

    }
}
