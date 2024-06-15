/**
 * AntiCactus Module
 */
package dev.zspacehack.module.modules.misc;

import dev.zspacehack.module.Module;

public class AntiCactus extends Module {
	public static AntiCactus INSTANCE;
	public AntiCactus() {
		super("AntiCactus", Category.Misc);
		this.setDescription("Prevents blocks from hurting you.");
		INSTANCE = this;
	}
}