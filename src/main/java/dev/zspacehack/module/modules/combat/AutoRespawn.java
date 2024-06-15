/**
 * AutoRespawn Module
 */
package dev.zspacehack.module.modules.combat;

import dev.zspacehack.module.Module;
import net.minecraft.client.gui.screen.DeathScreen;

public class AutoRespawn extends Module{
	
	public AutoRespawn() {
		super("AutoRespawn", Category.Combat);
		this.setDescription("Automatically respawns when you die.");
	}

	@Override
	public void onUpdate() {
		if (mc.currentScreen instanceof DeathScreen) {
			mc.player.requestRespawn();
			mc.setScreen(null);
		}
	}
}
