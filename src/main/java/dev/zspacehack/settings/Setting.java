/**
 * An abstract setting.
 */
package dev.zspacehack.settings;

public abstract class Setting {
	private final String name;
	private final String line;

	public Setting(String name, String line) {
		this.name = name;
		this.line = line;
	}
	/**
	 * Returns the name of the setting.
	 * @return The name of the setting.
	 */
	public final String getName() {
		return this.name;
	}
	
	/**
	 * Returns the codename of the setting.
	 * @return The codename of the setting.
	 */
	public final String getLine() {
		return this.line;
	}
	
	/**
	 * Abstract function to load the value of a given setting.
	 */
	public abstract void loadSetting();
}
