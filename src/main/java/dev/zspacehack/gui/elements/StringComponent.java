package dev.zspacehack.gui.elements;

import dev.zspacehack.gui.Color;
import dev.zspacehack.gui.tabs.ClickGuiTab;
import net.minecraft.client.gui.DrawContext;

public class StringComponent extends Component {

	private String text;
	
	public StringComponent(String text, ClickGuiTab parent) {
		super();
		this.text = text;
		this.parent = parent;
	}

	public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
		
	}

	@Override
	public void draw(int offset, DrawContext drawContext, float partialTicks, Color color) {
		int parentX = this.parent.getX();
		int parentY = this.parent.getY();
		currentOffset = animate(currentOffset, offset);
		renderUtils.drawString(drawContext, this.text, (float) (parentX + 10),
				(float) (parentY + 8 + currentOffset), 0xFFFFFF);
	}

	@Override
	public boolean unPoppedDraw(int offset, DrawContext drawContext, float partialTicks, Color color) {
		int parentX = this.parent.getX();
		int parentY = this.parent.getY();
		currentOffset = animate(currentOffset, offset);
		if (Math.abs(currentOffset - offset) <= 20) {
			return true;
		}
		renderUtils.drawString(drawContext, this.text, (float) (parentX + 10),
				(float) (parentY + 8 + currentOffset), 0xFFFFFF);
		return false;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}
	
	

}