package me.daxanius.npe.mixins.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.daxanius.npe.config.NoPryingEyesConfig;
import me.daxanius.npe.gui.NoPryingEyesWarningScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.message.MessageHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableTextContent;

@Mixin(MessageHandler.class)
public abstract class MessageHandlerMixin {
    @Inject(method = "onGameMessage", at = @At("HEAD"))
    private void onGameMessage(Text message, boolean overlay, CallbackInfo ci) {
        if(message.getContent() instanceof TranslatableTextContent translatable) {

            if(translatable.getKey().equals("chat.disabled.missingProfileKey") && NoPryingEyesConfig.getInstance().onDemand()) {
                Text warning = Text.translatable("npe.warning.on_demand");
                MinecraftClient.getInstance().setScreen(new NoPryingEyesWarningScreen(warning));
                NoPryingEyesConfig.getInstance().setTempSign(true);
            }
        }
        
            
        
    }
}
