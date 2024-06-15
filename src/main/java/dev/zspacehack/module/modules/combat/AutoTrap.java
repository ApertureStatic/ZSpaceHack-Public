package dev.zspacehack.module.modules.combat;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.Setting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.BlockUtil;
import dev.zspacehack.utils.CombatUtil;
import dev.zspacehack.utils.InventoryUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;

public class AutoTrap extends Module {
    private final SliderSetting Range = new SliderSetting("Range", 5, 1, 10, 1);
    private final BooleanSetting TrapOnHead = new BooleanSetting("TrapOnHead", false);
    private final BooleanSetting rotate = new BooleanSetting("Rotate", true);
    public AutoTrap() {
        super("AutoTrap", Category.Combat);
        try {
            for (Field field : AutoTrap.class.getDeclaredFields()) {
                if (!Setting.class.isAssignableFrom(field.getType()))
                    continue;
                Setting setting = (Setting) field.get(this);
                addSetting(setting);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;
        /*
        *      if (isValidPlace(targetPlacePos.down())) return targetPlacePos.down();
                else if (isValidPlace(targetPlacePos.up(2))) return targetPlacePos.up(2);
                else if (isValidPlace(targetPlacePos.add(1, 0, 0))) return targetPlacePos.add(1, 0, 0);
                else if (isValidPlace(targetPlacePos.add(-1, 0, 0))) return targetPlacePos.add(-1, 0, 0);
                else if (isValidPlace(targetPlacePos.add(0, 0, 1))) return targetPlacePos.add(0, 0, 1);
                else if (isValidPlace(targetPlacePos.add(0, 0, -1))) return targetPlacePos.add(0, 0, -1);
                else if (isValidPlace(targetPlacePos.add(1, 1, 0))) return targetPlacePos.add(1, 1, 0);
                else if (isValidPlace(targetPlacePos.add(-1, -1, 0))) return targetPlacePos.add(-1, -1, 0);
                else if (isValidPlace(targetPlacePos.add(0, 1, 1))) return targetPlacePos.add(0, 1, 1);
                else if (isValidPlace(targetPlacePos.add(0, 0, -1))) return targetPlacePos.add(0, 0, -1);
         */
        for (PlayerEntity target : CombatUtil.getEnemies(Range.getValueFloat())){
            if (target.isSpectator()) return;
            BlockPos pos = getPlacePos(target,Range.getValue());
            if (BlockUtil.canPlace(pos,Range.getValue())) {
                if (!TrapOnHead.getValue()) {
                    int obsidian = InventoryUtil.findBlock(Blocks.OBSIDIAN);
                    if (obsidian == -1) return;
                    InventoryUtil.doSwap(obsidian);
                    BlockUtil.placeBlock(pos, rotate.getValue());
                }
                else {
                    BlockUtil.placeBlock(target.getBlockPos().up(2), rotate.getValue());
                }
            }
        }
    }
    private BlockPos getPlacePos(PlayerEntity target,Double distance) {
        if (BlockUtil.canPlace(target.getBlockPos().down(1),distance)) return target.getBlockPos().down();
        else if (BlockUtil.canPlace(target.getBlockPos().up(2),distance)) return target.getBlockPos().up(2);
        else if (BlockUtil.canPlace(target.getBlockPos().add(1, 0, 0),distance)) return target.getBlockPos().add(1, 0, 0);
        else if (BlockUtil.canPlace(target.getBlockPos().add(-1, 0, 0),distance)) return target.getBlockPos().add(-1, 0, 0);
        else if (BlockUtil.canPlace(target.getBlockPos().add(0, 0, 1),distance)) return target.getBlockPos().add(0, 0, 1);
        else if (BlockUtil.canPlace(target.getBlockPos().add(0, 0, -1),distance)) return target.getBlockPos().add(0, 0, -1);
        else if (BlockUtil.canPlace(target.getBlockPos().add(1, 1, 0),distance)) return target.getBlockPos().add(1, 1, 0);
        else if (BlockUtil.canPlace(target.getBlockPos().add(-1, -1, 0),distance)) return target.getBlockPos().add(-1, -1, 0);
        else if (BlockUtil.canPlace(target.getBlockPos().add(0, 1, 1),distance)) return target.getBlockPos().add(0, 1, 1);
        else if (BlockUtil.canPlace(target.getBlockPos().add(0, 0, -1),distance)) return target.getBlockPos().add(0, 0, -1);
        return target.getBlockPos();
    }
    }

