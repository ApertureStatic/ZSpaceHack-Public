/**
 * A class to represent a system that manages all of the Modules.
 */
package dev.zspacehack.module;

import dev.zspacehack.gui.ClickUI;
import dev.zspacehack.module.modules.client.ClickGui;
import dev.zspacehack.module.modules.client.HUD;
import dev.zspacehack.module.modules.combat.*;
import dev.zspacehack.module.modules.misc.*;
import dev.zspacehack.module.modules.movement.*;
import dev.zspacehack.module.modules.render.*;
import dev.zspacehack.module.modules.world.AutoSign;
import dev.zspacehack.module.modules.world.Escer;
import dev.zspacehack.module.modules.world.SideMask;
import dev.zspacehack.module.modules.world.TileBreaker;
import dev.zspacehack.settings.Settings;
import dev.zspacehack.utils.RenderUtils;
import dev.zspacehack.utils.Wrapper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.Packet;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ModuleManager implements Wrapper {
	public ArrayList<Module> modules = new ArrayList<>();
	public static Module lastLoadModule;
	public ModuleManager() {
		// Look at all these modules!
		addModule(new ClickGui());
		addModule(new AutoCrystalPlus());
		addModule(new BowBomb());
		addModule(new PacketMine());
		addModule(new Aura());
		addModule(new AntiCactus());
		addModule(new AutoWeb());
		addModule(new HoleFiller());
		addModule(new HUD());
		addModule(new AutoTrap());
		addModule(new Escer());
		addModule(new SideMask());
		addModule(new AntiInvis());
		addModule(new Velocity());
		addModule(new AutoEat());
		addModule(new AutoFish());
		addModule(new AutoSign());
		addModule(new AutoRespawn());
		addModule(new AutoTotem());
		addModule(new FakeDmg());
		addModule(new WallClip());
		addModule(new AutoLog());
		addModule(new AutoCity());
		addModule(new AutoWalk());
		addModule(new Burrow());
		addModule(new ChestESP());
		addModule(new Surround());
		addModule(new EntityESP());
		addModule(new FastPlace());
		addModule(new Fullbright());
		addModule(new FakePlayer());
		addModule(new ItemESP());
		addModule(new InvMove());
		addModule(new NoFall());
		addModule(new NoRender());
		addModule(new NoSlowdown());
		addModule(new PlayerESP());
		addModule(new Reach());
		addModule(new Speed());
		addModule(new SpawnerESP());
		addModule(new Sprint());
		addModule(new Step());
		addModule(new TileBreaker());
		addModule(new Timer());
	}

	public void onKeyReleased(int eventKey) {
		if (eventKey == -1 || eventKey == 0 || mc.currentScreen instanceof ClickUI) {
			return;
		}
		modules.forEach(module -> {
			if (module.getBind().getKey() == eventKey) {
				module.disable();
			}
		});
	}

	public boolean setBind(int eventKey) {
		if (eventKey == -1 || eventKey == 0) {
			return false;
		}
		AtomicBoolean set = new AtomicBoolean(false);
		modules.forEach(module -> {
			if (module.getBind().isListening()) {
				module.getBind().setKey(eventKey);
				module.getBind().setListening(false);
				if (module.getBind().getBind().equals("DELETE")) {
					module.getBind().setKey(-1);
				}
				set.set(true);
			}
		});
		return set.get();
	}
	public void onKeyPressed(int eventKey) {
		if (eventKey == -1 || eventKey == 0 || mc.currentScreen instanceof ClickUI) {
			return;
		}
		modules.forEach(module -> {
			if (module.getBind().getKey() == eventKey) {
				module.toggle();
			}
		});
	}
	
	public void update() {
		for(Module module : modules) {
			if(module.isOn()) {
				module.onUpdate();
			}
		}
	}
	
	public void render(MatrixStack matrixStack) {
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_LINE_SMOOTH);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		matrixStack.push();
		RenderUtils.applyRenderOffset(matrixStack);
		for(Module module : modules) {
			if(module.isOn()) {
				module.onRender(matrixStack, MinecraftClient.getInstance().getTickDelta());
			}
		}
		matrixStack.pop();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glDisable(GL11.GL_LINE_SMOOTH);
	}
	
	public void sendPacket(Packet<?> packet) {
		for(Module module : modules) {
			if(module.isOn()) {
				module.onSendPacket(packet);
			}
		}
	}
	
	public void addModule(Module module) {
		modules.add(module);
		module.setState(Settings.getSettingBoolean(module.getName().toLowerCase() + "_state"));
		int key = Settings.getSettingInt(module.getName().toLowerCase() + "_bind");
		if (key == -1) return;
		module.getBind().setKey(key);
	}
	
	public void disableAll() {
		for(Module module : modules) {
			module.disable();
		}
	}
	
	public Module getModuleByName(String string) {
		for(Module module : modules) {
			if(module.getName().equalsIgnoreCase(string)) {
				return module;
			}
		}
		return null;
	}
}
