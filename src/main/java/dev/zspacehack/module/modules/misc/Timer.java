/**
 * Timer Module
 */
package dev.zspacehack.module.modules.misc;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.SliderSetting;
public class Timer extends Module {
	private SliderSetting multiplier;
	public static Timer INSTANCE;
	public Timer() {
		super("Timer", Category.Misc);
		this.setDescription("Increases the speed of Minecraft.");
		this.multiplier = new SliderSetting("Multiplier", "timer_multiplier", 1f, 0.1f, 15f, 0.1f);
		this.addSetting(multiplier);
		INSTANCE = this;
	}

	public float getMultiplier() {
		return this.multiplier.getValueFloat();
	}
	
	public void setMultipler(float multiplier) {
		this.multiplier.setValue(multiplier);
	}

}