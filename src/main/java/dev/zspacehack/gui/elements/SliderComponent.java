package dev.zspacehack.gui.elements;

import dev.zspacehack.gui.Color;
import dev.zspacehack.gui.HudManager;
import dev.zspacehack.gui.tabs.ClickGuiTab;
import dev.zspacehack.settings.SliderSetting;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

public class SliderComponent extends Component {

	private String text;
	private ClickGuiTab parent;
	private float currentSliderPosition = 0.4f;
	float r;
	float g;
	float b;
	
	SliderSetting slider;

	public SliderComponent(String text, ClickGuiTab parent) {
		super();
		this.text = text;
		this.parent = parent;
		this.slider = null;
	}

	public SliderComponent(ClickGuiTab parent, SliderSetting slider) {
		super();
		this.text = slider.getName();
		this.parent = parent;
		this.slider = slider;
		this.currentSliderPosition = (float) ((slider.getValue() - slider.getMinimum()) / slider.getRange());
	}

	@Override
	public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		if (HudManager.currentGrabbed == null) {
			if (mouseClicked) {
				if (mouseX >= ((parentX + 2)) && mouseX <= (((parentX)) + parentWidth - 2)) {
					if (mouseY >= (((parentY + offset)))
							&& mouseY <= ((parentY + offset) + 24)) {
						this.currentSliderPosition = (float) Math.min((((mouseX - ((parentX + 4))) - 1) / ((parentWidth - 12))), 1f);
						this.currentSliderPosition = Math.max(0f,this.currentSliderPosition);
						this.slider.setValue((this.currentSliderPosition * this.slider.getRange()) + this.slider.getMinimum());
					}
				}
			}
		}
	}

	private double renderSliderPosition = 0;

	@Override
	public void draw(int offset, DrawContext drawContext, float partialTicks, Color color) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		MatrixStack matrixStack = drawContext.getMatrices();
		currentOffset = animate(currentOffset, offset);
		renderSliderPosition = animate(renderSliderPosition, Math.floor((parentWidth - 6) * currentSliderPosition));
	//	RenderUtils.renderRoundedQuadInternal2(matrixStack.peek().getPositionMatrix(), 30,30,30,0.4f,30,30,30,0.4f,30,30,30,0.4f,30,30,30,0.4f,parentX + 3,(int) (parentY + currentOffset),parentX + parentWidth - 3,parentY + 24,7);
		renderUtils.drawBox(matrixStack, parentX + 3, (int) (parentY + currentOffset), parentWidth - 6, 24, 0.5f, 0.5f, 0.5f,
				0.3f);
		renderUtils.drawBox(matrixStack, parentX + 3, (int) (parentY + currentOffset),
				(int) this.renderSliderPosition, 24, color, 1f);
	//	renderUtils.drawOutline(matrixStack, parentX + 3, (int) (parentY + currentOffset), parentWidth - 6, 24);
		if(this.slider == null) return;
		renderUtils.drawString(drawContext, this.text + ": " + this.slider.getValueFloat(), (float) (parentX + 10),
				(float) (parentY + 6 + currentOffset), 0xFFFFFF);
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
		renderSliderPosition = animate(renderSliderPosition, Math.floor((parentWidth - 6) * currentSliderPosition));
		renderUtils.drawBox(matrixStack, parentX + 3, (int) (parentY + currentOffset), parentWidth - 6, 24, 0.5f, 0.5f, 0.5f,
				0.3f);
		renderUtils.drawBox(matrixStack, parentX + 3, (int) (parentY + currentOffset),
				(int) renderSliderPosition, 24, color, 1f);
		renderUtils.drawOutline(matrixStack, parentX + 3, (int) (parentY + currentOffset), parentWidth - 6, 24);
		if(this.slider == null) return false;
		renderUtils.drawString(drawContext, this.text + ": " + this.slider.getValueFloat(), parentX + 10,
				parentY + 6 + currentOffset, 0xFFFFFF);
		return false;
	}

	public float getSliderPosition() {
		return this.currentSliderPosition;
	}

	public void setSliderPosition(float pos) {
		this.currentSliderPosition = pos;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}

	public void setColor(float r, float g, float b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

}
