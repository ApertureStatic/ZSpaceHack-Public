/**
 * Reach Module
 */
package dev.zspacehack.module.modules.combat;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.SliderSetting;

public class Reach extends Module {
	public static Reach INSTANCE;
	private SliderSetting distance;
	
	public Reach() {
		super("Reach", Category.Combat);
		this.setDescription("Reaches further.");
		
		distance = new SliderSetting("Distance", "reach_distance", 5f, 1f, 15f, 1f);
		this.addSetting(distance);
		INSTANCE = this;
	}

	public float getReach() {
		return distance.getValueFloat();
	}

	public void setReachLength(float reach) {
		this.distance.setValue(reach);
	}
}