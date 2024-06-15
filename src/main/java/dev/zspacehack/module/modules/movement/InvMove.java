package dev.zspacehack.module.modules.movement;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;

public class InvMove extends Module {
    private final BooleanSetting sneak = new BooleanSetting("Sneak", "invmove_sneak");
    public InvMove() {
        super("InvMove", Category.Movement);
        this.setDescription("Walk in inventory.");

        addSetting(sneak);
    }

    @Override
    public void onRender(MatrixStack matrixStack, float partialTicks) {
        if (mc.currentScreen != null) {
            if (!(mc.currentScreen instanceof ChatScreen)) {
                for (KeyBinding k : new KeyBinding[] { mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey, mc.options.sprintKey }) {
                    k.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(k.getBoundKeyTranslationKey()).getCode()));
                }

                mc.options.forwardKey.setPressed(AutoWalk.INSTANCE.isOn() || InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.forwardKey.getBoundKeyTranslationKey()).getCode()));

                if (sneak.getValue()) {
                    mc.options.sneakKey.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        if (mc.currentScreen != null) {
            if (!(mc.currentScreen instanceof ChatScreen)) {
                for (KeyBinding k : new KeyBinding[] { mc.options.backKey, mc.options.leftKey, mc.options.rightKey, mc.options.jumpKey, mc.options.sprintKey }) {
                    k.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(k.getBoundKeyTranslationKey()).getCode()));
                }

                mc.options.forwardKey.setPressed(AutoWalk.INSTANCE.isOn() || InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.forwardKey.getBoundKeyTranslationKey()).getCode()));

                if (sneak.getValue()) {
                    mc.options.sneakKey.setPressed(InputUtil.isKeyPressed(mc.getWindow().getHandle(), InputUtil.fromTranslationKey(mc.options.sneakKey.getBoundKeyTranslationKey()).getCode()));
                }
            }
        }
    }
}
