package dev.zspacehack.gui;

import dev.zspacehack.ZSpace;
import dev.zspacehack.utils.Wrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickUI extends Screen implements Wrapper {

    public ClickUI() {
        super(Text.of("ClickGui"));
    }

    public static boolean clicked = false;
    public static boolean rightClicked = false;

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float partialTicks) {
        super.render(drawContext, mouseX, mouseY, partialTicks);
        ZSpace.HUD.draw(drawContext, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            clicked = true;
        } else if (button == 1) {
            rightClicked = true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            clicked = false;
        } else if (button == 1) {
            rightClicked = false;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        super.close();
        rightClicked = false;
        clicked = false;
    }
}
