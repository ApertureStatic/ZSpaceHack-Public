/**
 * AutoSign Module
 */
package dev.zspacehack.module.modules.world;

import dev.zspacehack.cmd.CommandManager;
import dev.zspacehack.module.Module;

public class AutoSign extends Module {
	public static AutoSign INSTANCE;
	String[] text;

	public AutoSign() {
		super("AutoSign", Category.World);
		this.setDescription("Automatically places sign.");
		INSTANCE = this;
	}

	public void setText(String[] text) {
		this.text = text;
	}
	
	public String[] getText() {
		return this.text;
	}

    @Override
	public void onEnable() {
		CommandManager.sendChatMessage("i\u00a7e[~] \u00a7fPlace down a sign to set text!");
		this.text = null;
	}
}
