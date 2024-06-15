package dev.zspacehack.gui;

import dev.zspacehack.ZSpace;
import dev.zspacehack.gui.elements.ModuleComponent;
import dev.zspacehack.gui.tabs.Tab;
import dev.zspacehack.module.Module;
import dev.zspacehack.module.modules.client.ClickGui;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.Setting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.gui.tabs.ClickGuiTab;
//import dev.zspacehack.gui.tabs.OptionsTab;
import dev.zspacehack.utils.RainbowColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Hashtable;

public class HudManager {

	public MinecraftClient mc;
	public static Hashtable<String, ClickGuiTab> tabs = new Hashtable<>();
	public static ClickUI clickGui = new ClickUI();
	public static IngameGUI hud;
	public ArmorHUD armorHud;
	public static Tab currentGrabbed = null;
	public static final ArrayList<Setting> SETTINGS = new ArrayList<>();
	private int lastMouseX = 0;
	private int lastMouseY = 0;
	private int mouseX;
	private int mouseY;

	//public OptionsTab optionsTab;
	
	public SliderSetting hue = new SliderSetting("Hue", "color_hue", 250, 0, 360, 1);
	public SliderSetting saturation = new SliderSetting("Saturation", "color_saturation", 0.2, 0, 1, 0.1);
	public SliderSetting effectSpeed = new SliderSetting("Effect Spd", "color_speed", 4, 1, 20, 0.1);
	public BooleanSetting rainbow = new BooleanSetting("Rainbow", "rainbow_mode");
	public BooleanSetting ah = new BooleanSetting("ArmorHUD", "armorhud_toggle");
	public BooleanSetting gear = new BooleanSetting("Gear", "gear_toggle", true);
	public BooleanSetting rotations = new BooleanSetting("ShowRotations", "rotation_toggle", true);
	public static Color currentColor;
	private final Color color;
	private final RainbowColor rainbowColor;

	public HudManager() {
		mc = MinecraftClient.getInstance();
		hud = new IngameGUI();
		armorHud = new ArmorHUD();
		color = new Color(hue.getValueFloat(), 1f, 1f);
		currentColor = color;
		rainbowColor = new RainbowColor();

	//	optionsTab = new OptionsTab("Options", 350, 500);
	//	optionsTab.addChild(hue);
		//optionsTab.addChild(saturation);
		//optionsTab.addChild(rainbow);
	//	optionsTab.addChild(ah);
	//	optionsTab.addChild(effectSpeed);
	//	optionsTab.addChild(gear);
	//	optionsTab.addChild(rotations);

		
		int xOffset = 320;
		for (Module.Category category : Module.Category.values()) {
			ClickGuiTab tab = new ClickGuiTab(category.name(), xOffset, 1);
			for (Module module : ZSpace.MODULE.modules) {
				if (module.getCategory() == category) {
					ModuleComponent button = new ModuleComponent(module.getName(), tab, module);
					tab.addChild(button);
				}
			}
			tabs.put(category.name(), tab);
			xOffset += tab.getWidth();
		}
		//tabs.put(optionsTab.getTitle(), optionsTab);

	}
	
	public Color getColor() {
		return currentColor;
	}
	
	public Color getOriginalColor() {
		return this.color;
	}
	
	public void update() {
		rainbow = ClickGui.INSTANCE.rainbow;
		hue = ClickGui.INSTANCE.hue;
		effectSpeed = ClickGui.INSTANCE.effectSpeed;
		boolean mouseClicked = mc.mouse.wasLeftButtonClicked();
		if (isClickGuiOpen()) {
			mouseClicked = ClickUI.clicked;
		}
		for (ClickGuiTab tab : tabs.values()) {
			if (isClickGuiOpen() || tab.getPinned()) {
				tab.update(mouseX, mouseY, mouseClicked);
			}
		}

		if (this.rainbow.getValue()) {
			rainbowColor.setEffectSpeed(this.effectSpeed.getValueFloat());
			rainbowColor.setSaturation(saturation.getValueFloat());
			currentColor = rainbowColor.getColor();
		} else {
			this.color.setHSV(hue.getValueFloat(), saturation.getValueFloat(), 1f);
			currentColor = color;
		}
	}

	public void draw(DrawContext drawContext, float tickDelta) {
				drawContext.getMatrices().push();
				drawContext.getMatrices().scale(0.5f, 0.5f, 1);
				boolean mouseClicked = mc.mouse.wasLeftButtonClicked();
				if (isClickGuiOpen()) {
					mouseClicked = ClickUI.clicked;
				}
				mouseX = (int) Math.ceil(mc.mouse.getX());
				mouseY = (int) Math.ceil(mc.mouse.getY());
				armorHud.update(mouseX, mouseY, mouseClicked);
				hud.update(mouseX, mouseY, mouseClicked);
				if (this.isClickGuiOpen()) {
					int dx = (int) (double) mouseX;
					int dy = (int) (double) mouseY;
					if (!mouseClicked)
						currentGrabbed = null;
					if (currentGrabbed != null)
						currentGrabbed.moveWindow((lastMouseX - dx), (lastMouseY - dy));
					this.lastMouseX = dx;
					this.lastMouseY = dy;
				}

				GL11.glDisable(GL11.GL_CULL_FACE);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

				MatrixStack matrixStack = drawContext.getMatrices();
				matrixStack.push();
				hud.draw(drawContext, tickDelta, currentColor);
				if (isClickGuiOpen()) {
					double quad = ClickGui.fade.easeOutQuad();
			if (ClickGui.INSTANCE.mode.getValue() == ClickGui.Mode.Scale) {
				matrixStack.scale((float) quad, (float) quad, 1);
			} else if (ClickGui.INSTANCE.mode.getValue() == ClickGui.Mode.Pull) {
				quad = 1 - quad;
				matrixStack.translate(0, -100 * quad, 0);
			}
		}
		for (ClickGuiTab tab : tabs.values()) {
			if (isClickGuiOpen() || tab.getPinned()) {
				tab.draw(drawContext, tickDelta, currentColor);
			}
		}
		matrixStack.pop();

		if (ah.getValue()) armorHud.draw(drawContext, tickDelta, currentColor);
		GL11.glEnable(GL11.GL_CULL_FACE);
		drawContext.getMatrices().pop();
	}

	public boolean isClickGuiOpen() {
		return mc.currentScreen instanceof ClickUI;
	}
}
