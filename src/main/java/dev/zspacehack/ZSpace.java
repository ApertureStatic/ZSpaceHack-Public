/**
 * A class to initialize and hold the Singleton of ZSpace Client.
 */
package dev.zspacehack;

import dev.zspacehack.altmanager.AltManager;
import dev.zspacehack.module.ModuleManager;
import dev.zspacehack.settings.Settings;
import dev.zspacehack.cmd.CommandManager;
import dev.zspacehack.events.eventbus.EventBus;
import dev.zspacehack.gui.HudManager;
import dev.zspacehack.module.modules.SubEvent;
import dev.zspacehack.utils.RenderUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.DrawContext;

import java.lang.invoke.MethodHandles;

public final class ZSpace implements ModInitializer {

	/**
	 * Initializes ZSpace Client and creates sub-systems.
	 */
	@Override
	public void onInitialize()
	{
		System.out.println("[" + ZSpace.NAME + "] Starting Client");
		System.out.println("[" + ZSpace.NAME + "] Register eventbus");
		EVENT_BUS.registerLambdaFactory("dev.zspacehack", (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
		RENDER = new RenderUtils();
		System.out.println("[" + ZSpace.NAME + "] Reading Settings");
		SETTINGS = new Settings();
		System.out.println("[" + ZSpace.NAME + "] Initializing Modules");
		MODULE = new ModuleManager();
		System.out.println("[" + ZSpace.NAME + "] Initializing Commands");
		COMMAND = new CommandManager();
		System.out.println("[" + ZSpace.NAME + "] Initializing GUI");
		HUD = new HudManager();
		System.out.println("[" + ZSpace.NAME + "] Loading Alts");
		ALT = new AltManager();
		System.out.println("[" + ZSpace.NAME + "] Loading SubEvent");
		SUB = new SubEvent();
		EVENT_BUS.subscribe(SUB);
		System.out.println("[" + ZSpace.NAME + "] Initialized and ready to play!");
	}

	public static final String NAME = "ZSpace";
	public static final String VERSION = "1.1.1";
	public static final String PREFIX = ".zspace";
	public static final EventBus EVENT_BUS = new EventBus();
	// Systems
	public static ModuleManager MODULE;
	public static CommandManager COMMAND;
	public static AltManager ALT;
	public static HudManager HUD;
	public static Settings SETTINGS;
	public static RenderUtils RENDER;
	public static SubEvent SUB;

	/**
	 * Updates ZSpace on a per-tick basis.
	 */
	public static void update() {
		MODULE.update();
		HUD.update();
	}

	/**
	 * Renders the HUD every frame
	 * @param context The current Matrix Stack
	 * @param partialTicks Delta between ticks
	 */
	public static void drawHUD(DrawContext context, float partialTicks) {
		// If the program is not in Ghost Mode, draw UI.
		if (!HUD.isClickGuiOpen()) HUD.draw(context, partialTicks);
	}

	/**
	 * Called when the client is shutting down.
	 */
	public static void endClient() {
		SETTINGS.saveSettings();
		ALT.saveAlts();
		System.out.println("[" + ZSpace.NAME + "] Shutting down...");
	}
}
