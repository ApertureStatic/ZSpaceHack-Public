/**
 * A class to represent a generic module.
 */
package dev.zspacehack.module;

import dev.zspacehack.settings.Setting;
import dev.zspacehack.ZSpace;
import dev.zspacehack.cmd.CommandManager;
import dev.zspacehack.settings.BindSetting;
import dev.zspacehack.utils.RenderUtils;
import dev.zspacehack.utils.Wrapper;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public abstract class Module implements Wrapper {

	public Module(String name, Category category) {
		this.name = name;
		this.category = category;
		setDescription(name);
		ModuleManager.lastLoadModule = this;
		keybind = new BindSetting(getName(), getName().toLowerCase() + "_bind", name.equalsIgnoreCase("ClickGui") ? GLFW.GLFW_KEY_Y : -1);
		addSetting(keybind);
	}
	private String name;
	private String description;
	private final Category category;
	private final BindSetting keybind;
	private boolean state;
	private final RenderUtils renderUtils = new RenderUtils();

	private final List<Setting> settings = new ArrayList<>();

	public String getName() {
		return this.name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Module.Category getCategory() {
		return this.category;
	}

	public BindSetting getBind() {
		return this.keybind;
	}


	public boolean isOn() {
		return this.state;
	}

	public void toggle() {
		if (this.isOn()) {
			disable();
		} else {
			enable();
		}
	}

	public void enable() {
		if (this.state) return;
		this.state = true;
		// send chat message to player
		if (mc.inGameHud != null){
			CommandManager.sendChatMessage(" " + this.getName() + " §aenabled");
		}
		ZSpace.EVENT_BUS.subscribe(this);
		this.onToggle();
		this.onEnable();
	}

	public void disable() {
		if (!this.state) return;
		this.state = false;
		// send chat message to player
		if (mc.inGameHud != null){
			CommandManager.sendChatMessage(" " + this.getName() + " §cdisabled");
		}
		ZSpace.EVENT_BUS.unsubscribe(this);
		this.onToggle();
		this.onDisable();
	}
	public void setState(boolean state) {
		if (this.state == state) return;
		if (state) {
			enable();
		} else {
			disable();
		}
	}

	public boolean setBind(String rkey) {
		if (rkey.equalsIgnoreCase("none")) {
			this.keybind.setKey(-1);
			return true;
		}
		int key;
		try {
			key = InputUtil.fromTranslationKey("key.keyboard." + rkey.toLowerCase()).getCode();
		} catch (NumberFormatException e) {
			if (!nullCheck()) CommandManager.sendChatMessage("\u00a7c[!] \u00a7fBad key!");
			return false;
		}
		if (rkey.equalsIgnoreCase("none")) {
			key = -1;
		}
		if (key == 0) {
			return false;
		}
		this.keybind.setKey(key);
		return true;
	}

	public Setting addSetting(Setting setting) {
		this.settings.add(setting);
		return setting;
	}

	public List<Setting> getSettings() {
		return this.settings;
	}

	public RenderUtils getRenderUtils() {
		return this.renderUtils;
	}

	public boolean hasSettings() {
		return !this.settings.isEmpty();
	}

	public static boolean nullCheck() {
		return mc.player == null || mc.world == null;
	}

	public void onDisable() {

	}

	public void onEnable() {

	}

	public void onToggle() {

	}

	public void onUpdate() {

	}

	public void onRender(MatrixStack matrixStack, float partialTicks) {

	}

	public void onSendPacket(Packet<?> packet) {

	}

	public final boolean isCategory(Module.Category category) {
		return category == this.category;
	}

	public enum Category {
		Combat, Movement, Render, World, Misc, Client
	}
}
