package me.daxanius.npe.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConfirmScreen;
import static net.minecraft.client.gui.screen.GameMenuScreen.disconnect;
import static net.minecraft.client.world.ClientWorld.QUITTING_MULTIPLAYER_TEXT;
import static net.minecraft.screen.ScreenTexts.*;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class NoPryingEyesWarningScreens {
    public static final ConfirmScreen REQUIRED_MESSAGE_AND_COMMAND_SIGNING = new NoPryingEyesWarningScreen(Text.translatable("npe.warning.server_key"));
    public static final ConfirmScreen REQUIRED_MESSAGE_SIGNING = new NoPryingEyesWarningScreen(Text.translatable("npe.warning.server_signing"));
    public static final ConfirmScreen ON_DEMAND_SIGNING_ENABLED = new NoPryingEyesWarningScreen(Text.translatable("npe.warning.on_demand"));

    private static class NoPryingEyesWarningScreen extends ConfirmScreen {
        public NoPryingEyesWarningScreen(Text message) {
            super(resumeGame -> {
                if (!resumeGame) {
                    disconnect(MinecraftClient.getInstance(), QUITTING_MULTIPLAYER_TEXT);
                } else {
                    MinecraftClient.getInstance().setScreen(null);
                }
            }, Text.translatable("npe.title"), message, ACKNOWLEDGE, DISCONNECT);
        }
    }
}