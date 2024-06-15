package dev.zspacehack.gui.elements;

import dev.zspacehack.gui.ClickUI;
import dev.zspacehack.gui.Color;
import dev.zspacehack.gui.HudManager;
import dev.zspacehack.gui.tabs.ClickGuiTab;
import dev.zspacehack.settings.EnumSetting;
import net.minecraft.client.gui.DrawContext;

public class EnumComponent extends Component {
	private final EnumSetting setting;

	public EnumComponent(ClickGuiTab parent, EnumSetting enumSetting) {
		super();
		this.parent = parent;
		setting = enumSetting;
	}

	public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		if (HudManager.currentGrabbed == null) {
			if (mouseClicked) {
				if (mouseX >= ((parentX + 2)) && mouseX <= (((parentX)) + parentWidth - 2)) {
					if (mouseY >= (((parentY + offset))) && mouseY <= ((parentY + offset) + 24)) {
						ClickUI.clicked = false;
						setting.increaseEnum();
					}
				}
			}
		}
	}

	@Override
	public void draw(int offset, DrawContext drawContext, float partialTicks, Color color) {
		int parentX = this.parent.getX();
		int parentY = this.parent.getY();
		currentOffset = animate(currentOffset, offset);
		renderUtils.drawString(drawContext, setting.getName() + ": " + setting.getValue().name(), (float) (parentX + 10),
				(float) (parentY + 6 + currentOffset), 0xFFFFFF);
	}

	@Override
	public boolean unPoppedDraw(int offset, DrawContext drawContext, float partialTicks, Color color) {
		int parentX = this.parent.getX();
		int parentY = this.parent.getY();
		currentOffset = animate(currentOffset, offset);
		if (Math.abs(currentOffset - offset) <= 20) {
			return true;
		}
		renderUtils.drawString(drawContext, this.setting.getName() + ": " + setting.getValue().name(), (float) (parentX + 10),
				(float) (parentY + 6 + currentOffset), 0xFFFFFF);
		return false;
	}
}