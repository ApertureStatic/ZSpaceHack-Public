/**
 * Fullbright Module
 */
package dev.zspacehack.module.modules.render;

import dev.zspacehack.module.Module;

public class Fullbright extends Module {
	public static Fullbright INSTANCE;
	public Fullbright() {
		super("FullBright", Category.Render);
		this.setDescription("Maxes out the brightness.");
		INSTANCE = this;
	}
}
