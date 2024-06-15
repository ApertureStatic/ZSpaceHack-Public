/**
 * NoSlowdown Module
 */
package dev.zspacehack.module.modules.movement;

import dev.zspacehack.module.Module;

public class NoSlowdown extends Module {
	public static NoSlowdown INSTANCE;
	public NoSlowdown() {
		super("NoSlowDown", Category.Movement);
		this.setDescription("Prevents the player from being slowed down by blocks.");
		INSTANCE = this;
	}
}
