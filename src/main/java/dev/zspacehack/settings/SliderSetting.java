/**
 * A class to represent a setting related to a Slider.
 */
package dev.zspacehack.settings;

import dev.zspacehack.module.ModuleManager;

public class SliderSetting extends Setting {

	private double value;
	private final double defaultValue;
	private final double minValue;
	private final double maxValue;
	private final double increment;

	public SliderSetting(String name, String line, double value, double min, double max, double increment) {
		super(name, line);
		this.value = value;
		this.defaultValue = value;
		this.minValue = min;
		this.maxValue = max;
		this.increment = increment;
		loadSetting();
	}

	public SliderSetting(String name, double value, double min, double max, double increment) {
		super(name, ModuleManager.lastLoadModule.getName().toLowerCase() + "_" + name.toLowerCase());
		this.value = value;
		this.defaultValue = value;
		this.minValue = min;
		this.maxValue = max;
		this.increment = increment;
		loadSetting();
	}

	public final double getValue() {
		return this.value;
	}

	public final float getValueFloat() {
		return (float) this.value;
	}

	public final int getValueInt() {
		return (int) this.value;
	}

	public final void setValue(double value) {
		this.value = this.increment * (Math.ceil(Math.abs(value / this.increment)));
	}

	public final double getMinimum() {
		return this.minValue;
	}

	public final double getMaximum() {
		return this.minValue;
	}

	public final double getIncrement() {
		return increment;
	}

	public final double getRange() {
		return this.maxValue - this.minValue;
	}

	@Override
	public void loadSetting() {
		this.value = Settings.getSettingFloat(this.getLine(), (float) this.defaultValue);
	}
}
