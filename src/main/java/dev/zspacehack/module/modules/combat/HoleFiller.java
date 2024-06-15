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

public class HoleFiller extends Module {
    private final BooleanSetting rotate
            = new BooleanSetting("Rotate",true);
    private final SliderSetting Range
            = new SliderSetting("Range", "Range", 5, 1, 10, 1);
    private final SliderSetting radius
            = new SliderSetting("radius", "radius", 1, 0, 2, 1);
    public HoleFiller(){
        super("HoleFiller",Category.Combat);
        try {
            for (Field field : HoleFiller.class.getDeclaredFields()) {
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
        if (mc.player.isSneaking()) return;
        for (PlayerEntity target : CombatUtil.getEnemies(Range.getValueFloat())){
            for (BlockPos blockPos : BlockUtil.getSphere(radius.getValueFloat(),target.getBlockPos())){
                if (blockPos.getY() - target.getBlockPos().getY() < 0){
                    int obsidian = InventoryUtil.findBlock(Blocks.OBSIDIAN);
                    if (obsidian == -1) return;
                    InventoryUtil.doSwap(obsidian);
                    if (BlockUtil.canPlace(blockPos,Range.getValue())) BlockUtil.placeBlock(blockPos,rotate.getValue());
                }
            }
        }
}
}
