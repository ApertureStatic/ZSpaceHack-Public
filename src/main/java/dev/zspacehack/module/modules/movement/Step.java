/**
 * Step Module
 */
package dev.zspacehack.module.modules.movement;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.SliderSetting;

public class Step extends Module {

	private SliderSetting stepHeight;
	
	public Step() {
		super("Step", Category.Movement);
		this.setDescription("Steps up blocks.");
		
		stepHeight = new SliderSetting("Height", "step_height", 1f, 0.0f, 2f, 0.5f);
		this.addSetting(stepHeight);
	}

	@Override
	public void onDisable() {
		if (nullCheck()) return;
		mc.player.setStepHeight(.5f);
	}

	@Override
	public void onUpdate() {
		if (nullCheck()) return;
		mc.player.setStepHeight(stepHeight.getValueFloat());
	}
}
