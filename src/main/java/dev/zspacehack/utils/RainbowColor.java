/**
 * A class to represent a Color that iterates.
 */
package dev.zspacehack.utils;

import dev.zspacehack.gui.Color;

public class RainbowColor {
	private float saturation = 1f;
	private final Color color;
	private final Timer timer = new Timer().reset();
	public RainbowColor() {
		this.color = new Color(255, 0, 0);
	}
	float effectSpeed;

	public void setEffectSpeed(float timerIncrement) {
		effectSpeed = timerIncrement;
	}

	public void setSaturation(float saturation) {
		this.saturation = saturation;
	}
	public Color getColor() {
		this.color.setHSV((float) (timer.getPassedTimeMs() * 0.36 * effectSpeed / 20) % 361, saturation, 1f);
		return this.color;
	}
}
