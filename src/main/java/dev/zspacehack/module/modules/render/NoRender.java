/**
 * NoOverlay Module
 */
package dev.zspacehack.module.modules.render;

import dev.zspacehack.events.ParticleEvent;
import dev.zspacehack.events.eventbus.EventHandler;
import dev.zspacehack.events.impl.PacketEvent;
import dev.zspacehack.events.impl.UpdateWalkingEvent;
import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.Setting;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.ElderGuardianAppearanceParticle;
import net.minecraft.client.particle.ExplosionLargeParticle;
import net.minecraft.client.particle.FireworksSparkParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.EggEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;

import java.lang.reflect.Field;

public class NoRender extends Module {
	public static NoRender INSTANCE;
	public BooleanSetting potions = new BooleanSetting("Potions", false);
	public BooleanSetting xp = new BooleanSetting("XP", false);
	public BooleanSetting arrows = new BooleanSetting("Arrows", false);
	public BooleanSetting eggs = new BooleanSetting("Eggs", false);
	public BooleanSetting armor = new BooleanSetting("Armor", false);
	public BooleanSetting hurtCam = new BooleanSetting("HurtCam", false);
	public BooleanSetting fireOverlay = new BooleanSetting("FireOverlay", false);
	public BooleanSetting waterOverlay = new BooleanSetting("WaterOverlay", false);
	public BooleanSetting blockOverlay = new BooleanSetting("BlockOverlay", false);
	public BooleanSetting nausea = new BooleanSetting("Nausea", false);
	public BooleanSetting blindness = new BooleanSetting("Blindness", false);
	public BooleanSetting fog = new BooleanSetting("Fog", false);
	public BooleanSetting darkness = new BooleanSetting("Darkness", false);
	public BooleanSetting fireEntity = new BooleanSetting("EntityFire", true);
	public BooleanSetting antiTitle = new BooleanSetting("Title", false);
	public BooleanSetting antiPlayerCollision = new BooleanSetting("PlayerCollision", true);
	public BooleanSetting elderGuardian = new BooleanSetting("Guardian", false);
	public BooleanSetting explosions = new BooleanSetting("Explosions", false);
	public BooleanSetting campFire = new BooleanSetting("CampFire", false);
	public BooleanSetting fireworks = new BooleanSetting("Fireworks", false);
	public NoRender() {
		super("NoRender", Category.Render);
		this.setDescription("Disables all overlays and potion effects.");
		INSTANCE = this;
		try {
			for (Field field : NoRender.class.getDeclaredFields()) {
				if (!Setting.class.isAssignableFrom(field.getType()))
					continue;
				Setting setting = (Setting) field.get(this);
				addSetting(setting);
			}
		} catch (Exception e) {
		}
	}

	@EventHandler
	public void onPacketReceive(PacketEvent.Receive e){
		if(e.getPacket() instanceof TitleS2CPacket && antiTitle.getValue()){
			e.setCancel(true);
		}
	}

	@EventHandler
	public void onUpdateWalking(UpdateWalkingEvent event) {
		for(Entity ent : mc.world.getEntities()){
			if(ent instanceof PotionEntity){
				if(potions.getValue())
					mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
			}
			if(ent instanceof ExperienceBottleEntity){
				if(xp.getValue())
					mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
			}
			if(ent instanceof ArrowEntity){
				if(arrows.getValue())
					mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
			}
			if(ent instanceof EggEntity){
				if(eggs.getValue())
					mc.world.removeEntity(ent.getId(), Entity.RemovalReason.KILLED);
			}
		}
	}

	@EventHandler
	public void onParticle(ParticleEvent.AddParticle event) {
		if (elderGuardian.getValue() && event.particle instanceof ElderGuardianAppearanceParticle) {
			event.setCancel(true);
		} else if (explosions.getValue() && event.particle instanceof ExplosionLargeParticle) {
			event.setCancel(true);
		} else if (campFire.getValue() && event.particle instanceof CampfireSmokeParticle) {
			event.setCancel(true);
		} else if (fireworks.getValue() && (event.particle instanceof FireworksSparkParticle.FireworkParticle || event.particle instanceof FireworksSparkParticle.Flash)) {
			event.setCancel(true);
		}
	}
}