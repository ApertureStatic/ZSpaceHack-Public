/**
 * PlayerESP Module
 */
package dev.zspacehack.module.modules.render;

import dev.zspacehack.gui.Color;
import dev.zspacehack.module.Module;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;

public class PlayerESP extends Module {

	public PlayerESP() {
		super("PlayerESP", Category.Render);
		this.setDescription("Allows the player to see other players with an ESP.");

	}

    @Override
	public void onRender(MatrixStack matrixStack, float partialTicks) {
		for (AbstractClientPlayerEntity entity : mc.world.getPlayers()) {
			if(entity != mc.player) {
				this.getRenderUtils().draw3DBox(matrixStack, entity.getBoundingBox(), new Color(255, 0, 0), 0.2f);
			}
		}
	}

}
