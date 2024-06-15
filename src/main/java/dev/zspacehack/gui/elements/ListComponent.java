package dev.zspacehack.gui.elements;

import dev.zspacehack.gui.ClickUI;
import dev.zspacehack.gui.Color;
import dev.zspacehack.gui.HudManager;
import dev.zspacehack.gui.tabs.ClickGuiTab;
import dev.zspacehack.settings.ListSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class ListComponent extends Component {

	private final ListSetting list;

	public ListComponent(ClickGuiTab parent, ListSetting list) {
		super();
		this.parent = parent;
		this.list = list;
	}

	@Override
	public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		if (HudManager.currentGrabbed == null) {
			if (mouseClicked) {
				if (mouseY >= (((parentY + offset + 4))) && mouseY <= (parentY + offset + 22)) {
					// If Left arrow clicked.
					if (mouseX >= ((parentX + 4)) && mouseX <= ((parentX + 64))) {
						ClickUI.clicked = false;
						list.decrement();
						return;
					}
					// If Right arrow clicked.
					if (mouseX >= ((parentX + parentWidth - 64)) && mouseX <= ((parentX + parentWidth - 4))) {
						ClickUI.clicked = false;
						list.increment();
					}
				}
			}
		}
	}

	@Override
	public void draw(int offset, DrawContext drawContext, float partialTicks, Color color) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		currentOffset = animate(currentOffset, offset);
		int length = MinecraftClient.getInstance().textRenderer.getWidth(list.getValue()); 
		MatrixStack matrixStack = drawContext.getMatrices();
		renderUtils.drawOutlinedBox(matrixStack, parentX + 4, (int) (parentY + currentOffset), parentWidth - 8, 22, new Color(25,25,25),
				0.3f);
		renderUtils.drawString(drawContext, list.getValue(), (float) ((parentX + (parentWidth / 2)) - length), (float) (parentY + currentOffset + 4), 0xFFFFFF);
		renderUtils.drawString(drawContext,"<<", (float) (parentX + 8), (float) (parentY + currentOffset + 4), 0xFFFFFF);
		renderUtils.drawString(drawContext,">>", (float) (parentX + 8 + (parentWidth - 34)), (float) (parentY + currentOffset + 4), 0xFFFFFF);
	}
}
