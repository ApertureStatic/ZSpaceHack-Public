/**
 * EntityESP Module
 */
package dev.zspacehack.module.modules.render;

import dev.zspacehack.gui.Color;
import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.CombatUtil;
import dev.zspacehack.utils.RainbowColor;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EntityESP extends Module {

	private Color color;
	private RainbowColor rainbowColor;

	
	public SliderSetting hue = new SliderSetting("Hue", "entityesp_hue", 4, 0, 360, 1);
	public BooleanSetting rainbow = new BooleanSetting("Rainbow", "entityesp_rainbow");
	public SliderSetting effectSpeed = new SliderSetting("Effect Spd", "entityesp_effectspeed", 4, 1, 20, 0.1);
	public BooleanSetting self = new BooleanSetting("Self", "entityesp_self");
	public EntityESP() {
		super("EntityESP", Category.Render);
		this.setDescription("Allows the player to see entities with an ESP.");
		color = new Color(255, 0, 0);
		rainbowColor = new RainbowColor();
		
		this.addSetting(hue);
		this.addSetting(rainbow);
		this.addSetting(effectSpeed);
		addSetting(self);
	}

    @Override
	public void onUpdate() {
		if(this.rainbow.getValue()) {
			this.rainbowColor.setEffectSpeed(this.effectSpeed.getValueFloat());
		}else {
			this.color.setHSV(hue.getValueFloat(), 1f, 1f);
		}
	}

	@Override
	public void onRender(MatrixStack matrixStack, float partialTicks) {		
		for (Entity entity : mc.world.getEntities()) {
			if (entity instanceof LivingEntity && !(entity instanceof PlayerEntity)) {
				if (entity instanceof AnimalEntity) {
					this.getRenderUtils().draw3DBox(matrixStack, entity.getBoundingBox(), new Color(0, 255, 0), 0.2f);
				} else if (entity instanceof Monster) {
					this.getRenderUtils().draw3DBox(matrixStack, entity.getBoundingBox(), new Color(255, 0, 0), 0.2f);
				} else {
					this.getRenderUtils().draw3DBox(matrixStack, entity.getBoundingBox(), new Color(0, 0, 255), 0.2f);
				}
			}
		}
		if (self.getValue()) {
			this.getRenderUtils().draw3DBox(matrixStack, mc.player.getBoundingBox().offset(CombatUtil.getMotionVec(mc.player, 4, true)), new Color(0, 0, 255), 0.2f);
		}
	}

}
