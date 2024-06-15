package dev.zspacehack.utils;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MovementUtil implements Wrapper{
    public static boolean isMoving() {
        return mc.player.input.movementForward != 0.0 || mc.player.input.movementSideways != 0.0;
    }

    public static boolean isJumping() {
        return mc.options.jumpKey.isPressed();
    }

    public static double getDistance2D() {
        double xDist = mc.player.getX() - mc.player.prevX;
        double zDist = mc.player.getZ() - mc.player.prevZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public static double getJumpSpeed() {
        double defaultSpeed = 0.0;

        if (mc.player.hasStatusEffect(StatusEffect.byRawId(8))) {
            //noinspection ConstantConditions
            int amplifier = mc.player.getActiveStatusEffects().get(StatusEffect.byRawId(8)).getAmplifier();
            defaultSpeed += (amplifier + 1) * 0.1;
        }

        return defaultSpeed;
    }
    public static double getMoveForward() {
        return mc.player.input.movementForward;
    }

    public static double getMoveStrafe() {
        return mc.player.input.movementSideways;
    }

    public static boolean isInMovementDirection(double x, double y, double z) {
        if (getMotionX() != 0.0 || getMotionZ() != 0.0) {
            BlockPos movingPos = PlayerUtil.getPlayerPos()
                    .add((int) (getMotionX() * 10000), 0, (int) (getMotionZ() * 10000));

            BlockPos antiPos = PlayerUtil.getPlayerPos()
                    .add((int) (getMotionX() * -10000), 0, (int) (getMotionY() * -10000));

            return movingPos.getSquaredDistance(x, y, z) < antiPos.getSquaredDistance(x, y, z);
        }

        return true;
    }

    public static double getMotionX() {
        return mc.player.getVelocity().x;
    }
    public static double getMotionY() {
        return mc.player.getVelocity().y;
    }
    public static double getMotionZ() {
        return mc.player.getVelocity().z;
    }
    public static void setMotionX(double x) {
        Vec3d velocity = new Vec3d(x, mc.player.getVelocity().y, mc.player.getVelocity().z);
        mc.player.setVelocity(velocity);
    }
    public static void setMotionY(double y) {
        Vec3d velocity = new Vec3d(mc.player.getVelocity().x, y, mc.player.getVelocity().z);
        mc.player.setVelocity(velocity);
    }
    public static void setMotionZ(double z) {
        Vec3d velocity = new Vec3d(mc.player.getVelocity().x, mc.player.getVelocity().y, z);
        mc.player.setVelocity(velocity);
    }

    public static double getSpeed(boolean slowness) {
        double defaultSpeed = 0.2873;
        return getSpeed(slowness, defaultSpeed);
    }

    public static double getSpeed(boolean slowness, double defaultSpeed) {
        if (mc.player.hasStatusEffect(StatusEffect.byRawId(1))) {
            int amplifier = mc.player.getActiveStatusEffects().get(StatusEffect.byRawId(1))
                    .getAmplifier();

            defaultSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }

        if (slowness && mc.player.hasStatusEffect(StatusEffect.byRawId(2))) {
            int amplifier = mc.player.getActiveStatusEffects().get(StatusEffect.byRawId(2))
                    .getAmplifier();

            defaultSpeed /= 1.0 + 0.2 * (amplifier + 1);
        }

        if (mc.player.isSneaking()) defaultSpeed /= 5;
        return defaultSpeed;
    }
}
