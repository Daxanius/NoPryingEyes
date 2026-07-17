package me.daxanius.npe.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.Component;

import static net.minecraft.client.multiplayer.ClientLevel.DEFAULT_QUIT_MESSAGE;
import static net.minecraft.network.chat.CommonComponents.GUI_ACKNOWLEDGE;
import static net.minecraft.network.chat.CommonComponents.GUI_DISCONNECT;

public class NoPryingEyesWarningScreens {
    public static final ConfirmScreen REQUIRED_MESSAGE_AND_COMMAND_SIGNING = new NoPryingEyesWarningScreen(Component.translatable("npe.warning.server_key"));
    public static final ConfirmScreen REQUIRED_MESSAGE_SIGNING = new NoPryingEyesWarningScreen(Component.translatable("npe.warning.server_signing"));
    public static final ConfirmScreen ON_DEMAND_SIGNING_ENABLED = new NoPryingEyesWarningScreen(Component.translatable("npe.warning.on_demand"));

    private static class NoPryingEyesWarningScreen extends ConfirmScreen {
        public NoPryingEyesWarningScreen(Component message) {
            super(resumeGame -> {
                Minecraft client = Minecraft.getInstance();
                if (!resumeGame) {
                    client.disconnectFromWorld(DEFAULT_QUIT_MESSAGE);
                } else {
                    Minecraft.getInstance().gui.setScreen(null);
                }
            }, Component.translatable("npe.title"), message, GUI_ACKNOWLEDGE, GUI_DISCONNECT);
        }
    }
}