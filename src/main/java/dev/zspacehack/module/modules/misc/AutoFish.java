/**
 * AutoFish Module
 */
package dev.zspacehack.module.modules.misc;

import dev.zspacehack.events.eventbus.EventHandler;
import dev.zspacehack.events.impl.PacketEvent;
import dev.zspacehack.module.Module;
import dev.zspacehack.utils.Wrapper;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;

public class AutoFish extends Module {
	public AutoFish() {
		super("AutoFish", Category.Misc);
		this.setDescription("Automatically fishes for you.");
	}

	@EventHandler
	public void onReceivePacket(PacketEvent.Receive event) {
		Packet<?> packet = event.getPacket();
		if(packet instanceof PlaySoundS2CPacket soundPacket) {
			if(soundPacket.getSound().value().equals(SoundEvents.ENTITY_FISHING_BOBBER_SPLASH)) {
				recastRod();
			}
		}
	}
	
	private void recastRod() {
		
		PlayerInteractItemC2SPacket packetTryUse = new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0);
		Wrapper.mc.player.networkHandler.sendPacket(packetTryUse);
		Wrapper.mc.player.networkHandler.sendPacket(packetTryUse);
	}

}
