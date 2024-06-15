package dev.zspacehack.module.modules.combat;

import dev.zspacehack.cmd.CommandManager;
import dev.zspacehack.gui.Color;
import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.Setting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.BlockUtil;
import dev.zspacehack.utils.CombatUtil;
import dev.zspacehack.utils.InventoryUtil;
import dev.zspacehack.utils.Timer;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.lang.reflect.Field;

public class AnchorAuraTest extends Module {

    private final BooleanSetting pause =

            new BooleanSetting("PauseOnUse", false);
    private final BooleanSetting rotations =
            new BooleanSetting("Rotations", true);
    private final BooleanSetting placehelper =
            new BooleanSetting("placehelper", false);
    private final SliderSetting Range =
            new SliderSetting("Range", 5, 1, 10, 1);
    private final SliderSetting predictTicks =
            new SliderSetting("PredictTicks", "anchoraura_predictticks", 2, 0.0, 10, 1);
    private final SliderSetting delay =
            new SliderSetting("Delay", 1, 0, 10, 0.1);
    private final SliderSetting MinDamage
            = new SliderSetting("MinDamage", 10, 0, 100, 1);
    private final SliderSetting TotalHealth
            = new SliderSetting("TotalHealth", 10, 0, 20, 1);
    private final SliderSetting radius =
            new SliderSetting("radius", 1, 0, 2, 1);
    private final SliderSetting selfdmg
            = new SliderSetting("SelfDmg", 10, 0, 36, 1);

    public AnchorAuraTest() {
        super("AnchorAuraTest", Category.Combat);
        try {
            for (Field field : AnchorAuraTest.class.getDeclaredFields()) {
                if (!Setting.class.isAssignableFrom(field.getType()))
                    continue;
                Setting setting = (Setting) field.get(this);
                addSetting(setting);
            }
        } catch (Exception e) {
        }
    }

    private final
    Timer timer
            = new
            Timer().reset();
    BlockPos
            blockPos
            = null;
    PlayerEntity
            player
            = null;

    @Override
    public void onRender(MatrixStack matrixStack, float partialTicks) {
        if (blockPos != null) {
            this.getRenderUtils().draw3DBox(matrixStack, new Box(blockPos), new Color(255, 255, 255), 0.4f);
        }
        if (player != null) {
            this.getRenderUtils().draw3DBox(matrixStack, new Box(player.getBlockPos()), new Color(255, 255, 255), 0.4f);
        }
    }

    @Override
    public void onEnable() {
        if (mc.world == null) return;
        if (mc.world.getDimension().respawnAnchorWorks()) {
            disable();
            CommandManager.sendChatMessage("disabling... anchor work on the nether");
        }
    }

    @Override
    public void onUpdate() {
        // TODO 封装对象
        boolean
                pause
                = this.pause.getValue();
        boolean
                rotations
                = this.rotations.getValue();
        boolean
                placehelper
                = this.placehelper.getValue();
        double
                range
                = Range.getValue();
        double
                predictTicks
                = this.predictTicks.getValue();
        double
                minDamage
                = MinDamage.getValue();

        double
                radius
                = this.radius.getValue();
        double
                selfdmg
                = this.selfdmg.getValue();
        /*
            TODO AnchorAura Rewrite
         */
        int
                anchor_blocks
                = InventoryUtil
                .findBlock(Blocks.RESPAWN_ANCHOR);
        int
                glowstone_blocks
                = InventoryUtil
                .findBlock(Blocks.GLOWSTONE);
        int
                unBlock
                = InventoryUtil
                .findUnBlock();
        if
        (anchor_blocks
                == -1 ||
                glowstone_blocks == -1) return;
        if
        (!timer.passed((long) (delay.getValueFloat()
                * 1000))) {
            return;
        }
        //  if
        // (hltdmgget
        //        ((int)totalHealth)) return;
        if (pause && mc.player.isUsingItem()) return;
        for (PlayerEntity
                enemy : CombatUtil.getEnemies(range)) {
            for (BlockPos blockPos :
                    BlockUtil.getSphere((float) radius, enemy.getBlockPos())) {
                if (enemy.isSpectator()) return;
                if (CombatUtil.getAnchorDamage(blockPos, mc.player, (int) predictTicks, true) > selfdmg) return;
                if (BlockUtil.canPlace(blockPos, range)) {
                    doSwapAndPlace(anchor_blocks, glowstone_blocks, blockPos, unBlock, rotations);
                    this.blockPos = blockPos;
                } else if (placehelper && !BlockUtil.canBreak(blockPos,mc.world.getBlockState(blockPos))) {
                    // TODO
                    //  place help block
                    int obsidian = InventoryUtil.findBlock(Blocks.OBSIDIAN);
                    if (BlockUtil.canPlace(blockPos.north(), range)) {
                        InventoryUtil.doSwap(obsidian);
                        BlockUtil.placeBlock(blockPos.north(), rotations);
                    }
                    else if (BlockUtil.canPlace(blockPos.south(), range)) {
                        InventoryUtil.doSwap(obsidian);
                        BlockUtil.placeBlock(blockPos.south(), rotations);
                    }
                    else if (BlockUtil.canPlace(blockPos.east(), range)) {
                        InventoryUtil.doSwap(obsidian);
                        BlockUtil.placeBlock(blockPos.east(), rotations);
                    }
                    else if (BlockUtil.canPlace(blockPos.west(), range)) {
                        InventoryUtil.doSwap(obsidian);
                        BlockUtil.placeBlock(blockPos.west(), rotations);
                    }
                }
            }
            timer.reset();
        }
    }
    /**
     * 执行交换和放置的私有方法
     * @param anchor_blocks 锚定方块的数量
     * @param glowstone_blocks 发光石方块的数量
     * @param pos 块的位置
     * @param unBlock 需要交换的方块类型
     * @param rotate 是否旋转
     */
    private void doSwapAndPlace(int anchor_blocks,int glowstone_blocks,BlockPos pos,int unBlock,boolean rotate) {
        InventoryUtil.
                doSwap(anchor_blocks);
        BlockUtil.
                placeBlock(pos, rotations.getValue());
        InventoryUtil.
                doSwap(glowstone_blocks);
        doexp(pos);
        InventoryUtil.
                doSwap(unBlock);
        BlockUtil
                .clickBlock(pos, BlockUtil.getPlaceSide(pos), rotations.getValue());
    }
    private void doexp(BlockPos blockpos){
        int glowstone1 = InventoryUtil.findBlock(Blocks.GLOWSTONE);
        if (glowstone1 == -1) return;
        InventoryUtil.doSwap(glowstone1);
        mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,new BlockHitResult(blockpos.toCenterPos(), Direction.UP, blockpos, false));
    }
}
