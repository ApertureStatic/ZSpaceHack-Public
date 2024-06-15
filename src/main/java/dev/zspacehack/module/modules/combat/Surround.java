package dev.zspacehack.module.modules.combat;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.Setting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.BlockUtil;
import dev.zspacehack.utils.InventoryUtil;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

import java.lang.reflect.Field;

public class Surround extends Module {
    private final SliderSetting radius
            = new SliderSetting("Radius", 2, 0, 2, 1);
    private final BooleanSetting rotations = new BooleanSetting("Rotations", true);

    public Surround() {
        super("Surround", Category.Combat);
        try {
            for (Field field : Surround.class.getDeclaredFields()) {
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
        if (nullCheck()) return;
        if (mc.player == null) return;
        if (mc.player.isSpectator()) return;
        int obsidian
                = InventoryUtil.findBlock(Blocks.OBSIDIAN);
        if (obsidian == -1) return;
        InventoryUtil.doSwap(obsidian);
        for (BlockPos blockPos : BlockUtil.getSphere((float) radius.getValue(), mc.player.getBlockPos())) {
            BlockUtil.placeBlock(blockPos, rotations.getValue());
        }
    }
}


