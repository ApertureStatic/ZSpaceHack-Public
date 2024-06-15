/**
 * FastPlace Module
 */
package dev.zspacehack.module.modules.misc;

import dev.zspacehack.module.Module;
import dev.zspacehack.utils.Wrapper;

public class FastPlace extends Module {
	public FastPlace() {
		super("FastPlace", Category.Misc);
		this.setDescription("Places blocks exceptionally fast");
	}

	@Override
	public void onDisable() {
		Wrapper.imc.setItemUseCooldown(4);
	}

    @Override
	public void onUpdate() {
		Wrapper.imc.setItemUseCooldown(0);
	}

}
