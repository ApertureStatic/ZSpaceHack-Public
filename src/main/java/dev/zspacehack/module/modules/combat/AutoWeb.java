package dev.zspacehack.module.modules.combat;

import dev.zspacehack.gui.Color;
import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.Setting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.BlockUtil;
import dev.zspacehack.utils.CombatUtil;
import dev.zspacehack.utils.InventoryUtil;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.lang.reflect.Field;

public class AutoWeb extends Module {
    private final SliderSetting RenderR = new SliderSetting("RenderR", "RenderR", 255, 0, 255 ,1);
    private final SliderSetting RenderG = new SliderSetting("RenderG", "RenderG", 255, 0, 255 ,1);
    private final SliderSetting RenderB = new SliderSetting("RenderB", "RenderB", 255, 0, 255 ,1);
    private final SliderSetting Range = new SliderSetting("Range", "Range", 5, 1, 10, 1);
    private final BooleanSetting rotate = new BooleanSetting("Rotate", "Rotate", true);
    public AutoWeb(){
        super( "AutoWeb", Category.Combat);
        try {
            for (Field field : AutoWeb.class.getDeclaredFields()) {
                if (!Setting.class.isAssignableFrom(field.getType()))
                    continue;
                Setting setting = (Setting) field.get(this);
                addSetting(setting);
            }
        } catch (Exception e) {
        }
    }
    BlockPos blockPos;
    BlockPos blockPos1;
    @Override
    public void onRender(MatrixStack matrixStack, float partialTicks) {
        if (blockPos != null) {
            this.getRenderUtils().draw3DBox(matrixStack,new Box(blockPos),new Color(RenderR.getValueFloat(),RenderB.getValueFloat(),RenderG.getValueFloat()),0.4f);
        }
        if (blockPos1 != null) {
            this.getRenderUtils().draw3DBox(matrixStack,new Box(blockPos1),new Color(RenderR.getValueFloat(),RenderB.getValueFloat(),RenderG.getValueFloat()),0.4f);
        }
    }
    @Override
    public void onUpdate() {
        for (PlayerEntity target : CombatUtil.getEnemies(Range.getValueFloat())){
            int cob_web = InventoryUtil.findBlock(Blocks.COBWEB);
            if (cob_web == -1) {
                return;
            }
            blockPos = target.getBlockPos();
            blockPos1 = target.getBlockPos().add(0,-1,0);
            InventoryUtil.doSwap(cob_web);
            BlockUtil.placeBlock(blockPos,rotate.getValue());
            BlockUtil.placeBlock(blockPos1,rotate.getValue());
            }
        }
    }


