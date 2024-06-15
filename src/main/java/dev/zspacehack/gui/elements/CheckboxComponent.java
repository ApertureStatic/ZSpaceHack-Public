package dev.zspacehack.gui.elements;

import dev.zspacehack.gui.ClickUI;
import dev.zspacehack.gui.Color;
import dev.zspacehack.gui.HudManager;
import dev.zspacehack.gui.tabs.ClickGuiTab;
import dev.zspacehack.settings.BooleanSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class CheckboxComponent extends Component {
	private String text;
	
	BooleanSetting checkbox;
	
	public CheckboxComponent(ClickGuiTab parent, BooleanSetting checkbox) {
		super();
		this.text = checkbox.getName();
		this.parent = parent;
		this.checkbox = checkbox;
	}

	@Override
	public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		if (HudManager.currentGrabbed == null) {
			if (mouseClicked) {
				if (mouseX >= ((parentX + 2)) && mouseX <= (((parentX)) + parentWidth - 2)) {
					if (mouseY >= (((parentY + offset))) && mouseY <= ((parentY + offset) + 24)) {
						ClickUI.clicked = false;
						checkbox.toggleValue();
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
		MatrixStack matrixStack = drawContext.getMatrices();
		currentOffset = animate(currentOffset, offset);
		renderUtils.drawString(drawContext, this.text, (float) (parentX + 10),
				(float) (parentY + currentOffset + 6), 0xFFFFFF);
		if(this.checkbox.getValue()) {
			renderUtils.drawOutlinedBox(matrixStack, parentX + parentWidth - 24, (int) (parentY + 1 + currentOffset), 20, 20, color, 0.8f);
		}else {
			renderUtils.drawOutlinedBox(matrixStack, parentX + parentWidth - 24, (int) (parentY + 1 + currentOffset), 20, 20, new Color(128,128,128), 0.8f);
		}
	}

	@Override
	public boolean unPoppedDraw(int offset, DrawContext drawContext, float partialTicks, Color color) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		MatrixStack matrixStack = drawContext.getMatrices();
		currentOffset = animate(currentOffset, offset);
		if (Math.abs(currentOffset - offset) <= 20) {
			return true;
		}
		renderUtils.drawString(drawContext, this.text, (float) (parentX + 10),
				(float) (parentY + currentOffset + 6), 0xFFFFFF);
		if(this.checkbox.getValue()) {
			renderUtils.drawOutlinedBox(matrixStack, parentX + parentWidth - 24, (int) (parentY + 1 + currentOffset), 20, 20, color, 0.8f);
		}else {
			renderUtils.drawOutlinedBox(matrixStack, parentX + parentWidth - 24, (int) (parentY + 1 + currentOffset), 20, 20, new Color(128,128,128), 0.8f);
		}
		return false;
	}
}
