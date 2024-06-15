/**
 * SpawnerESP Module
 */
package dev.zspacehack.module.modules.render;

import dev.zspacehack.gui.Color;
import dev.zspacehack.module.Module;
import dev.zspacehack.utils.ModuleUtils;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SpawnerESP extends Module {

	public SpawnerESP() {
		super("SpwanerESP", Category.Render);
		this.setDescription("Allows the player to see spawners with an ESP.");
	}

    @Override
	public void onRender(MatrixStack matrixStack, float partialTicks) {
		ArrayList<BlockEntity> blockEntities = ModuleUtils.getTileEntities().collect(Collectors.toCollection(ArrayList::new));
		
		for(BlockEntity blockEntity : blockEntities) {
			if(blockEntity instanceof MobSpawnerBlockEntity) {
				Box box = new Box(blockEntity.getPos());
				this.getRenderUtils().draw3DBox(matrixStack, box, new Color(255,255,0), 0.2f);
			}
		}
	}

}