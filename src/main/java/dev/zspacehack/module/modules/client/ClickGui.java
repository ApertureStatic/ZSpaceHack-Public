/**
 * Anti-Invis Module
 */
package dev.zspacehack.module.modules.client;

import dev.zspacehack.gui.ClickUI;
import dev.zspacehack.gui.HudManager;
import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.EnumSetting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.FadeUtils;

public class ClickGui extends Module {
	public static ClickGui INSTANCE;
	public EnumSetting mode = new EnumSetting("Mode", Mode.Pull);
	public BooleanSetting rainbow = new BooleanSetting("Rainbow", false);
	public SliderSetting hue = new SliderSetting("Hue", 250, 0, 360, 1);
	public SliderSetting effectSpeed = new SliderSetting("Effect Spd", 4, 1, 20, 0.1);
	public ClickGui() {
		super("ClickGui", Category.Client);
		INSTANCE = this;
		addSetting(mode);
		addSetting(rainbow);
		addSetting(hue);
		addSetting(effectSpeed);
	}

	public static final FadeUtils fade = new FadeUtils(400);

	@Override
	public void onUpdate() {
		if (!(mc.currentScreen instanceof ClickUI)) {
			disable();
		}
	}

	@Override
	public void onEnable() {
		fade.reset();
		if (nullCheck()) {
			disable();
			return;
		}
		mc.setScreen(HudManager.clickGui);
	}

	@Override
	public void onDisable() {
		if (mc.currentScreen instanceof ClickUI) {
			mc.setScreen(null);
		}
	}

	public enum Mode {
		Scale, Pull
	}
}