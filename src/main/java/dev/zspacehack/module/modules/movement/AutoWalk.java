/**
 * AutoWalk Module
 */
package dev.zspacehack.module.modules.movement;

import dev.zspacehack.module.Module;
import dev.zspacehack.utils.Wrapper;

public class AutoWalk extends Module {
	public static AutoWalk INSTANCE;
	public AutoWalk() {
		super("AutoWalk", Category.Movement);
		INSTANCE = this;
	}

	@Override
	public void onDisable() {
		Wrapper.mc.options.forwardKey.setPressed(false);
	}

	@Override
	public void onUpdate() {
		Wrapper.mc.options.forwardKey.setPressed(true);
	}

}
