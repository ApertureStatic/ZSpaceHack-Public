package dev.zspacehack.gui.elements;

import dev.zspacehack.gui.ClickUI;
import dev.zspacehack.gui.Color;
import dev.zspacehack.gui.HudManager;
import dev.zspacehack.gui.tabs.ClickGuiTab;
import dev.zspacehack.module.Module;
import dev.zspacehack.settings.*;
import dev.zspacehack.utils.RenderUtils;
import dev.zspacehack.utils.Wrapper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class ModuleComponent extends Component implements Wrapper {

	private final String text;
	private final Module module;
	private final ClickGuiTab parent;
	private boolean popped = false;

	private int expandedHeight = 30;
	
	private final Color hoverColor = new Color(90, 90, 90);
	private final Color color = new Color(128, 128, 128);

	private Color backgroundColor = color;

	private final List<Component> settingsList = new ArrayList<>();

	public ModuleComponent(String text, ClickGuiTab parent, Module module) {
		super();
		this.text = text;
		this.parent = parent;
		this.module = module;
		for (Setting setting : this.module.getSettings()) {
			Component c;
			if (setting instanceof SliderSetting) {
				c = new SliderComponent(this.parent, (SliderSetting) setting);
			} else if (setting instanceof BooleanSetting) {
				c = new CheckboxComponent(this.parent, (BooleanSetting) setting);
			} else if (setting instanceof ListSetting) {
				c = new ListComponent(this.parent, (ListSetting) setting);
			} else if (setting instanceof BindSetting) {
				c = new BindComponent(this.parent, (BindSetting) setting);
			} else if (setting instanceof EnumSetting) {
				c = new EnumComponent(this.parent, (EnumSetting) setting);
			} else {
				c = null;
			}
			settingsList.add(c);
		}
		
		RecalculateExpandedHeight();
	}

	public void update(int offset, double mouseX, double mouseY, boolean mouseClicked) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();

		// If the Module options are popped, display all of the options.
		if (this.popped) {
			// Updates all of the options. 
			int i = offset + 30;
			for (Component children : this.settingsList) {
				children.update(i, mouseX, mouseY, mouseClicked);
				i += children.getHeight();
			}
		}


		// Check if the current Module Component is currently hovered over.
		boolean hovered = ((mouseX >= parentX && mouseX <= (parentX + parentWidth)) && (mouseY >= parentY + offset && mouseY <= (parentY + offset + 28)));
		if (hovered && HudManager.currentGrabbed == null) {
			backgroundColor = hoverColor;
			if (mouseClicked) {
				ClickUI.clicked = false;
				module.toggle();
			}

			if (ClickUI.rightClicked) {
				ClickUI.rightClicked = false;
				this.popped = !this.popped;
				if (this.popped) {
					this.setHeight(expandedHeight);
				} else {
					this.setHeight(30);
				}
			}
		} else {
			backgroundColor = color;
		}
	}

	boolean isPopped = false;
	double currentHeight = 0;

	@Override
	public void draw(int offset, DrawContext drawContext, float partialTicks, Color color) {
		int parentX = parent.getX();
		int parentY = parent.getY();
		int parentWidth = parent.getWidth();
		MatrixStack matrixStack = drawContext.getMatrices();
		currentOffset = animate(currentOffset, offset);
		currentHeight = animate(currentHeight, getHeight());
		//RenderUtils.renderRoundedQuad2
		// (matrixStack
		// ,new java.awt.Color(20,20,20)
		// ,new java.awt.Color(20,20,20)
		// ,new java.awt.Color(20,20,20)
		// ,new java.awt.Color(20,20,20)
		// ,parentX+2,
		// (int)
		// (parentY + currentOffset)
		// ,parentX + parentWidth - 2,(int)
		// (parentY + currentOffset) +  (int)
		// (currentHeight - 2),7f);
		RenderUtils.renderRoundedQuadInternal2(matrixStack.peek().getPositionMatrix(), 20,20,20,0.4f,20,20,20,20,20,20,20,0.4f,20,20,20,0.4f,parentX + 2,parentY + currentOffset,parentX + parentWidth - 2,(parentY + currentOffset + currentHeight - 2),7f);

		if (this.popped) {
			isPopped = true;
			int i = offset + 30;
			for (Component children : this.settingsList) {
				if (children.isVisible()) {
					children.draw(i, drawContext, partialTicks, color);
					i += children.getHeight();
				}
			}
		} else if (isPopped) {
			boolean finish2 = true;
			boolean finish = false;
			for (Component children : this.settingsList) {
				if (children.isVisible()) {
					if (children.unPoppedDraw((int) currentOffset, drawContext, partialTicks, color)) {
						finish = true;
					} else {
						finish2 = false;
					}
				}
			}
			if (finish && finish2) {
				isPopped = false;
			}
		} else {
			for (Component children : this.settingsList) {
				children.currentOffset = currentOffset;
			}
		}

		renderUtils.drawString(drawContext, this.text, (float) (parentX + 8), (float) (parentY + 8 + currentOffset),
				module.isOn() ? color.getColorAsInt() : 0xFFFFFFFF);
	}
	
	public void RecalculateExpandedHeight() {
		int height = 30;
		for (Component children : this.settingsList) {
			if (children.isVisible()) {
				height += children.getHeight();
			}
		}
		expandedHeight = height;
	}
}
