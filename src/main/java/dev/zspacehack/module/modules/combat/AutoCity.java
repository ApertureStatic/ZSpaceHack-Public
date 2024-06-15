package dev.zspacehack.module.modules.combat;

import dev.zspacehack.gui.Color;
import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.EnumSetting;
import dev.zspacehack.settings.Setting;
import dev.zspacehack.settings.SliderSetting;
import dev.zspacehack.utils.BlockUtil;
import dev.zspacehack.utils.CombatUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.lang.reflect.Field;

import static dev.zspacehack.module.modules.combat.AutoCity.Mode.*;

public class AutoCity extends Module {
    private final SliderSetting Range = new SliderSetting("Range", 5, 1, 10, 1);
    private final BooleanSetting Rotate = new BooleanSetting("Rotate",true);
    private final EnumSetting mode = new EnumSetting("Mode", packet);
    public AutoCity(){
        super("AutoCity",Category.Combat);
        try {
            for (Field field : AutoCity.class.getDeclaredFields()) {
                if (!Setting.class.isAssignableFrom(field.getType()))
                    continue;
                Setting setting = (Setting) field.get(this);
                addSetting(setting);
            }
        } catch (Exception e) {
        }
    }
    public enum Mode{
        packet,
        normal,
        always
    }
    BlockPos blockPos;
    @Override
    public void onRender(MatrixStack matrixStack, float partialTicks) {
        if (blockPos != null){
            this.getRenderUtils().draw3DBox(matrixStack,new Box(blockPos),new Color(255,255,255),0.4f);
        }
    }
    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.isSpectator()) return;
        for (BlockPos blockPos : BlockUtil.getSphere(Range.getValueFloat())){
            for (PlayerEntity target : CombatUtil.getEnemies(Range.getValueFloat())){
                if (target.isSpectator()) {
                    return;
                }
                if (mode.getValue().equals(packet)) {
                    if (PacketMine.INSTANCE.isOn()){
                        if (!BlockUtil.canPlace(blockPos,Range.getValue())){
                            BlockUtil.breakBlock(blockPos);
                        }
                    }

                } else if (mode.getValue().equals(normal)) {
                    if (!BlockUtil.canPlace(blockPos,Range.getValue())){
                        BlockUtil.breakBlock(blockPos);
                        mc.player.networkHandler.sendPacket(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, blockPos, BlockUtil.getClickSide(blockPos)));
                    }
                } else if (mode.getValue().equals(always)) {
                    if (!BlockUtil.canPlace(blockPos,Range.getValue())){
                        BlockUtil.breakBlock(blockPos);
                    }
                }

            }
        }

        }
    }


