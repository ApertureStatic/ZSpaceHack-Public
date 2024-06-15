package dev.zspacehack.module.modules.world;

import dev.zspacehack.module.Module;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class SideMask extends Module {
    public SideMask(){
        super("SideMask",Category.World);
    }
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.isSpectator()) return;
        float rotationSpeed = 360.0F;
        float radiansPerTick = ((float) Math.PI * 2) * rotationSpeed / 20.0F;
        float newYaw = mc.player.getYaw() + radiansPerTick;
        float newPitch = mc.player.getPitch();

        // do rotations
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(newYaw,newPitch, mc.player.isOnGround()));


    }
}
