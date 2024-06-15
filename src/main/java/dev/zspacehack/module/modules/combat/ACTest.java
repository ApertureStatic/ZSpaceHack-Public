package dev.zspacehack.module.modules.combat;

import dev.zspacehack.gui.Color;
import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.Setting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.BlockUtil;
import dev.zspacehack.utils.CombatUtil;
import dev.zspacehack.utils.InventoryUtil;
import dev.zspacehack.utils.Timer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.lang.reflect.Field;

import static dev.zspacehack.utils.BlockUtil.getLegitRotations;

public class ACTest extends Module {
    private final BooleanSetting InsideBlocks
            = new BooleanSetting("InsideBlocks", true);
    private final BooleanSetting pause
            = new BooleanSetting("PauseOnUse", false);
    private final BooleanSetting safe_armor
            = new BooleanSetting("SafeArmor", false);
    private final BooleanSetting rotate
            = new BooleanSetting("Rotate", true);
    private final BooleanSetting IgnoreSafeTarget
            = new BooleanSetting("Safe", false);
    private final BooleanSetting off_hand
            = new BooleanSetting("offhand", false);
    private final BooleanSetting swinghand
            = new BooleanSetting("swing", true);
    private final SliderSetting radius
            = new SliderSetting("radius",2, 1, 5, 1);
    private final SliderSetting Range
            = new SliderSetting("Range", 5, 1, 10, 1);
    private final SliderSetting PlaceDelay
            = new SliderSetting("PlaceDelay", 1, 0, 10, 0.1);
    private final SliderSetting MinDmg
            = new SliderSetting("MinDmg", 10, 1, 20, 1);
    private final SliderSetting TotalHealth
            = new SliderSetting("TotalHealth", 10, 1, 20, 1);
    // TODO: 没写完
    public ACTest(){
        super("ACTest",Category.Combat);
        try {
            for (Field field : ACTest.class.getDeclaredFields()) {
                if (!Setting.class.isAssignableFrom(field.getType()))
                    continue;
                Setting setting = (Setting) field.get(this);
                addSetting(setting);
            }
        } catch (Exception e) {
        }
    }
    Timer timer = new Timer().reset();
    BlockPos blockPos;
    @Override
    public void onUpdate() {
        if (nullCheck()) return;
        if (!timer.passed((long) (PlaceDelay.getValueFloat() * 1000))) {
            return;
        }
        if (pause.getValue() && mc.player.isUsingItem()) return;
        if (safe_armor.getValue() && mc.player.getInventory().armor.get(0).isEmpty()
                && mc.player.getInventory().armor.get(1).isEmpty()
                && mc.player.getInventory().armor.get(2).isEmpty()
                && mc.player.getInventory().armor.get(3).isEmpty()) {
            return;
        }
        if (mc.player.getHealth() <= TotalHealth.getValue()) return;
        for (PlayerEntity target : CombatUtil.getEnemies(Range.getValue())){
            for (BlockPos blockPos1 : BlockUtil.getSphere((float)radius.getValue(),target.getBlockPos())){
                breakCrystal(Range.getValue());
                if (IgnoreSafeTarget.getValue()
                        && mc.player.getInventory().armor.get(0).isEmpty()
                        && mc.player.getInventory().armor.get(1).isEmpty()
                        && mc.player.getInventory().armor.get(2).isEmpty()
                        && mc.player.getInventory().armor.get(3).isEmpty())
                    return;
                if (blockPos1.equals(target.getBlockPos())) return;
                int item = InventoryUtil.findItem(Items.END_CRYSTAL);
                if (CombatUtil.getDamage(blockPos1.toCenterPos(),mc.player) > MinDmg.getValue()) return;
                if (BlockUtil.canPlace(blockPos1, Range.getValue())){
                    BlockPos supportBlockpos
                            = getSupportBlockpos(blockPos1);
                    BooleanSetting swinghand1
                            = swinghand;
                    placeCrystal
                            (supportBlockpos,item, swinghand1.getValue(),Direction.UP);
                    breakCrystal
                            (Range.getValue());
                    blockPos = supportBlockpos;
                }
            }
            timer.reset();
            }
        }
    public void onRender(MatrixStack matrixStack, float partialTicks) {
        if (blockPos != null){
            this.getRenderUtils().draw3DBox(matrixStack,new Box(blockPos),new Color(255,255,255),0.4f);
        }
    }
    private void breakCrystal(Double range) {
        for (Entity target : mc.world.getEntities()) {
            if (target instanceof EndCrystalEntity) {
                if (mc.player.distanceTo(target) < range) {
                    mc.player.swingHand(off_hand.getValue() ? Hand.MAIN_HAND : Hand.OFF_HAND);
                    mc.player.networkHandler.sendPacket(PlayerInteractEntityC2SPacket.attack(target, mc.player.isSneaking()));
                }
            }
        }
    }
    private void placeCrystal(BlockPos blockPos1,int crystal,boolean swinghand,Direction side){
        Vec3d directionVec = new Vec3d
                (blockPos1.getX()
                        + 0.5
                        + side.getVector().getX()
                        * 0.5, blockPos1.getY() + 0.5
                        + side.getVector().getY() * 0.5,
                        blockPos1.getZ()
                                + 0.5 + side.getVector().getZ()
                                * 0.5);
        if (rotate.getValue()) {
            float[] angle = getLegitRotations(directionVec);
            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(angle[0], angle[1], mc.player.isOnGround()));
        }
        if (crystal == -1){
            return;
        }
        int old = InventoryUtil.findItem(!off_hand.getValue()?mc.player.getMainHandStack().getItem():mc.player.getOffHandStack().getItem());
        InventoryUtil.doSwap(crystal);
        if (swinghand) mc.player.swingHand(off_hand.getValue()?Hand.OFF_HAND:Hand.MAIN_HAND);
        mc.interactionManager.interactBlock(mc.player,Hand.MAIN_HAND,new BlockHitResult(blockPos1.toCenterPos(), side,blockPos1,InsideBlocks.getValue()));
        InventoryUtil.doSwap(old);
    }
    private BlockPos getSupportBlockpos(BlockPos blockPos1){
        return blockPos1.add(0,-1,0);
    }
}
