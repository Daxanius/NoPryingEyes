package daxanius.npe.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.WarningScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class NoPryingEyesWarningScreen extends WarningScreen {
    private static final Text TITLE = Text.translatable("npe.title").setStyle(Style.EMPTY.withBold(true));

    public NoPryingEyesWarningScreen(Text content) {
        super(TITLE, content.copy().setStyle(Style.EMPTY.withColor(16776960)), TITLE.copy().append("\n").append(content));
    }

    protected void initButtons(int yOffset) {
        int width = 150;
        this.addDrawableChild(new ButtonWidget(this.width / 2 - width / 2, 100 + yOffset, width, 20, ScreenTexts.ACKNOWLEDGE, (button) -> {
            MinecraftClient.getInstance().disconnect();
            MinecraftClient.getInstance().setScreen(new MultiplayerScreen(new TitleScreen()));
        }));
    }

    @Override
    protected void drawTitle(MatrixStack matrices) {
        drawTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2 - this.title.toString().length(), 30, 16777215);
    }
}