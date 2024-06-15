/**
 * AntiCactus Module
 */
package dev.zspacehack.module.modules.misc;

import com.mojang.authlib.GameProfile;
import dev.zspacehack.module.Module;
import dev.zspacehack.utils.Wrapper;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;

import java.util.UUID;

public class FakePlayer extends Module {
	public static FakePlayer INSTANCE;
	public FakePlayer() {
		super("FakePlayer", Category.Misc);
		this.setDescription("Spawn fakeplayer.");
		INSTANCE = this;
	}

	public static OtherClientPlayerEntity fakePlayer;

	@Override
	public void onEnable() {
		if (nullCheck()) {
			disable();
			return;
		}
		fakePlayer = new OtherClientPlayerEntity(Wrapper.mc.world, new GameProfile(UUID.fromString("11451466-6666-6666-6666-666666666600"), "FakePlayer"));
		fakePlayer.copyPositionAndRotation(Wrapper.mc.player);
		fakePlayer.getInventory().clone(Wrapper.mc.player.getInventory());
		Wrapper.mc.world.addPlayer(-1, fakePlayer);
	}



	@Override
	public void onDisable() {
		if(fakePlayer == null) return;
		fakePlayer.kill();
		fakePlayer.setRemoved(Entity.RemovalReason.KILLED);
		fakePlayer.onRemoved();
	}
}