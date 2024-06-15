package dev.zspacehack.gui.elements;

import dev.zspacehack.ZSpace;
import dev.zspacehack.gui.Color;
import dev.zspacehack.gui.tabs.ClickGuiTab;
import dev.zspacehack.utils.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

public abstract class Component {
	protected RenderUtils renderUtils;
	protected ClickGuiTab parent;
	private int height = 30;
	private boolean visible = true;
	
	public Component() {
		this.renderUtils = ZSpace.RENDER;
	}
	
	public void setVisible(boolean bool) {
		this.visible = bool;
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setHeight(int height)
	{
		this.height = height;
	}
	
	public ClickGuiTab getParent()
	{
		return parent;
	}
	
	public void setParent(ClickGuiTab parent)
	{
		this.parent = parent;
	}

	public abstract void update(int offset, double mouseX, double mouseY, boolean mouseClicked);
	public abstract void draw(int offset, DrawContext drawContext, float partialTicks, Color color);
	public boolean unPoppedDraw(int offset, DrawContext drawContext, float partialTicks, Color color) {
		this.currentOffset = offset;
		return true;
	}
	public double currentOffset = 0;

	public double animate(double value, double target) {
		double add = (target - value) / ((double) (3 * Math.max(Math.min(240, MinecraftClient.getInstance().getCurrentFps()), 120)) / 60);
		if (Math.abs(add) <= 1) return target;
		return value + add;
	}
}
