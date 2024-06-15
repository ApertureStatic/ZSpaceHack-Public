package dev.zspacehack.module.modules.combat;

import dev.zspacehack.module.Module;
import dev.zspacehack.utils.BlockUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class PacketMine extends Module {
    public static PacketMine INSTANCE;
    public PacketMine(){
        super("PacketMine", Category.Combat);
        INSTANCE = this;
    }
    @Override
    public void onUpdate() {
        if (!isPlayerMining(mc.player, mc.world)) return;
        BlockPos breakPos = getPlayerMiningBlockPos(mc.player, mc.world);
        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, breakPos, BlockUtil.getClickSide(breakPos)));
    }
    public static boolean isPlayerMining(PlayerEntity player, World world) {
        RaycastContext context = new RaycastContext(player.getEyePos(), player.getRotationVec(1).normalize(), RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, player);
        BlockHitResult hitResult = world.raycast(context);
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return true;
        }
        return false;
    }

    public static BlockPos getPlayerMiningBlockPos(PlayerEntity player, World world) {
        RaycastContext context = new RaycastContext(player.getEyePos(), player.getRotationVec(1).normalize(), RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, player);
        BlockHitResult hitResult = world.raycast(context);
        return hitResult.getBlockPos();
    }
}
