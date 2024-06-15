package dev.zspacehack.module.modules.combat;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.CombatUtil;
import dev.zspacehack.utils.Timer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import static dev.zspacehack.utils.BlockUtil.getLegitRotations;

public class Aura extends Module {
    private final SliderSetting range
            = new SliderSetting("Range", "Range", 5.0, 0.0, 6.0, 0.1);
    private final SliderSetting delay
            = new SliderSetting("Delay", "Delay", 0.7, 0.0, 1.0, 0.1);
    public Aura() {
        super("Aura", Category.Combat);
        addSetting(range);
        addSetting(delay);
    }
    private final Timer timer = new Timer().reset();
    @Override
    public void onUpdate() {
        if (!timer.passed((long) (delay.getValueFloat() * 1000))) {
            return;
        }
        update();
    }
    private void update() {
        for (PlayerEntity target : CombatUtil.getEnemies(range.getValue())) {
            if (isHoldingSwordOrAxe(mc.player)){
                if (target.isDead()) return;
                if (target.isSpectator()) return;
                mc.player.swingHand(Hand.MAIN_HAND);
                //mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));
                mc.player.attack(target);
                rotate(target.getBlockPos(), target.getHorizontalFacing());
            }
        }
            timer.reset();
    }

    private boolean isHoldingSwordOrAxe(PlayerEntity player) {
        ItemStack heldItem = player.getMainHandStack();
        return isItemSword(heldItem) || isItemAxe(heldItem);
    }
    private boolean isItemSword(ItemStack itemStack) {
        return itemStack.getItem() instanceof SwordItem;
    }
    private boolean isItemAxe(ItemStack itemStack) {
        return itemStack.getItem() instanceof AxeItem;
    }
    private void rotate(BlockPos blockPos, Direction side) {
        Vec3d directionVec = new Vec3d(blockPos.getX()
                        + 0.5
                        + side.getVector().getX()
                        * 0.5, blockPos.getY() + 0.5
                        + side.getVector().getY() * 0.5,
                        blockPos.getZ()
                                + 0.5 + side.getVector().getZ()
                                * 0.5);
        float[] angle = getLegitRotations(directionVec);
        mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], mc.player.isOnGround()));
        }
    }

