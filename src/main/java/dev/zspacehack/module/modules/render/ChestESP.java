/**
 * ChestESP Module
 */
package dev.zspacehack.module.modules.render;

import dev.zspacehack.gui.Color;
import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.ModuleUtils;
import dev.zspacehack.utils.RainbowColor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.TrappedChestBlockEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ChestESP extends Module {
	private Color currentColor;
	private Color color;
	private RainbowColor rainbowColor;

	public SliderSetting hue = new SliderSetting("Hue", "chestesp_hue", 4, 0, 360, 1);
	public BooleanSetting rainbow = new BooleanSetting("Rainbow", "chestesp_rainbow");
	public SliderSetting effectSpeed = new SliderSetting("Effect Speed", "chestesp_effectspeed", 4, 1, 20, 0.1);
	
	public ChestESP() {
		super("ChestESP", Category.Render);
		this.setDescription("Allows the player to see Chests with an ESP.");
		color = new Color(hue.getValueFloat(), 1f, 1f);
		currentColor = color;
		rainbowColor = new RainbowColor();
		this.addSetting(hue);
		this.addSetting(rainbow);
		this.addSetting(effectSpeed);
	}

    @Override
	public void onUpdate() {
		if(this.rainbow.getValue()) {
			this.rainbowColor.setEffectSpeed(this.effectSpeed.getValueFloat());
			this.currentColor = this.rainbowColor.getColor();
		}else {
			this.color.setHSV(hue.getValueFloat(), 1f, 1f);
			this.currentColor = color;
		}
	}

	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks) {
		ArrayList<BlockEntity> blockEntities = ModuleUtils.getTileEntities().collect(Collectors.toCollection(ArrayList::new));
		for(BlockEntity blockEntity : blockEntities) {
			if(blockEntity instanceof ChestBlockEntity || blockEntity instanceof TrappedChestBlockEntity) {
				Box box = new Box(blockEntity.getPos());
				this.getRenderUtils().draw3DBox(matrixStack, box, currentColor, 0.2f);
			}
		}
	}

}
