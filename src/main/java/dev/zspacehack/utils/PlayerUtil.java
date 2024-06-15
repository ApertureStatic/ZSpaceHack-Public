package dev.zspacehack.utils;

import net.minecraft.util.math.BlockPos;

public class PlayerUtil implements Wrapper {
    public static BlockPos getPlayerPos() {
        return new BlockPos((int) mc.player.getX(), (int) mc.player.getY(), (int) mc.player.getZ());
    }
}
