/**
 * Anti-Invis Module
 */
package dev.zspacehack.module.modules.combat;

import dev.zspacehack.module.Module;

public class AntiInvis extends Module {
	public static AntiInvis INSTANCE;
	public AntiInvis() {
		super("AntiInvis", Category.Combat);
		this.setDescription("Reveals players who are invisible.");
		INSTANCE = this;
	}

}