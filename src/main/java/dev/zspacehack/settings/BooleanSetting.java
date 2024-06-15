/**
 * An setting that holds a variable of true or false.
 */
package dev.zspacehack.settings;

import dev.zspacehack.module.ModuleManager;

public class BooleanSetting extends Setting{
	private boolean value;
	private final boolean defaultValue;
	
	public BooleanSetting(String name, String line) {
		super(name, line);
		this.defaultValue = false;
		this.loadSetting();
	}

	public BooleanSetting(String name, String line, boolean defaultValue) {
		super(name, line);
		this.defaultValue = defaultValue;
		this.loadSetting();
	}

	public BooleanSetting(String name, boolean defaultValue) {
		super(name, ModuleManager.lastLoadModule.getName().toLowerCase() + "_" + name.toLowerCase());
		this.defaultValue = defaultValue;
		this.loadSetting();
	}

	public final boolean getValue() {
		return this.value;
	}
	
	public final void setValue(boolean value) {
		this.value = value;
	}
	
	public final void toggleValue() {
		this.value = !value;
	}

	@Override
	public void loadSetting() {
		this.value = Settings.getSettingBoolean(this.getLine(), defaultValue);
	}
}
