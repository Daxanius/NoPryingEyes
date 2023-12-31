package me.daxanius.npe.mixins.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import me.daxanius.npe.config.NoPryingEyesConfig;
import net.minecraft.server.ServerMetadata;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.reflect.Type;

@Mixin(ServerMetadata.Deserializer.class)
public class ServerMetadataDeserializerMixin {
    @Inject(method = "serialize(Lnet/minecraft/server/ServerMetadata;Ljava/lang/reflect/Type;Lcom/google/gson/JsonSerializationContext;)Lcom/google/gson/JsonElement;", at = @At("RETURN"))
    private void onSerialize(ServerMetadata metadata, Type type, JsonSerializationContext context,
                             CallbackInfoReturnable<JsonElement> info) {
        ((JsonObject) info.getReturnValue()).addProperty("preventsChatReports", NoPryingEyesConfig.getInstance().noSign());
    }
}
