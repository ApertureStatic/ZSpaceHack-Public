package dev.zspacehack.module.modules;

import dev.zspacehack.module.Module;
import dev.zspacehack.events.Event;
import dev.zspacehack.events.eventbus.EventHandler;
import dev.zspacehack.events.eventbus.EventPriority;
import dev.zspacehack.events.impl.PacketEvent;
import dev.zspacehack.events.impl.UpdateWalkingEvent;
import dev.zspacehack.utils.Wrapper;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;

public class SubEvent implements Wrapper {
    private static float renderPitch;
    private static float renderYawOffset;
    private static float prevPitch;
    private static float prevRenderYawOffset;
    private static float prevRotationYawHead;
    private static float rotationYawHead;
    private int ticksExisted;
    private float lastYaw = 0;
    private float lastPitch = 0;

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerMoveC2SPacket packet) {
            float yaw = packet.getYaw(114514);
            float pitch = packet.getPitch(114514);
            if (yaw == 114514 || pitch == 114514) return;
            lastYaw = yaw;
            lastPitch = pitch;
            set(lastYaw, lastPitch);
        }
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void onReceivePacket(PacketEvent.Receive event) {
        if (event.getPacket() instanceof PlayerPositionLookS2CPacket packet) {
            lastYaw = packet.getYaw();
            lastPitch = packet.getPitch();
            set(packet.getYaw(), packet.getPitch());
        }
    }
    @EventHandler
    public void onUpdateWalking(UpdateWalkingEvent event) {
        if (event.getStage() != Event.Stage.Post) {
            return;
        }
        set(lastYaw, lastPitch);
    }
    private void set(float yaw, float pitch) {
        if (Module.nullCheck()) return;
        if (mc.player.age == ticksExisted) {
            return;
        }

        ticksExisted = mc.player.age;
        prevPitch = renderPitch;

        prevRenderYawOffset = renderYawOffset;
        renderYawOffset = getRenderYawOffset(yaw, prevRenderYawOffset);

        prevRotationYawHead = rotationYawHead;
        rotationYawHead = yaw;

        renderPitch = pitch;
    }

    public static float getRenderPitch() {
        return renderPitch;
    }

    public static float getRotationYawHead() {
        return rotationYawHead;
    }

    public static float getRenderYawOffset() {
        return renderYawOffset;
    }

    public static float getPrevPitch() {
        return prevPitch;
    }

    public static float getPrevRotationYawHead() {
        return prevRotationYawHead;
    }

    public static float getPrevRenderYawOffset() {
        return prevRenderYawOffset;
    }

    private float getRenderYawOffset(float yaw, float offsetIn) {
        float result = offsetIn;
        float offset;

        double xDif = mc.player.getX() - mc.player.prevX;
        double zDif = mc.player.getZ() - mc.player.prevZ;

        if (xDif * xDif + zDif * zDif > 0.0025000002f) {
            offset = (float) MathHelper.atan2(zDif, xDif) * 57.295776f - 90.0f;
            float wrap = MathHelper.abs(MathHelper.wrapDegrees(yaw) - offset);
            if (95.0F < wrap && wrap < 265.0F) {
                result = offset - 180.0F;
            } else {
                result = offset;
            }
        }

        if (mc.player.handSwingProgress > 0.0F) {
            result = yaw;
        }

        result = offsetIn + MathHelper.wrapDegrees(result - offsetIn) * 0.3f;
        offset = MathHelper.wrapDegrees(yaw - result);

        if (offset < -75.0f) {
            offset = -75.0f;
        } else if (offset >= 75.0f) {
            offset = 75.0f;
        }

        result = yaw - offset;
        if (offset * offset > 2500.0f) {
            result += offset * 0.2f;
        }

        return result;
    }
}
