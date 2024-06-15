package dev.zspacehack.module.modules.misc;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.SliderSetting;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.text.Text;

public class AutoLog extends Module {
    private final SliderSetting health =
            new SliderSetting("Health", 10, 1, 36, 1);
    public AutoLog() {
        super("AutoLog",Category.Misc);
        addSetting(health);
    }
    @Override
    public void onUpdate() {
        if (mc.player.getHealth() < health.getValueFloat()){
            onDisconnect();
            disable();
        }
    }
    private void onDisconnect() {
        if (mc.player == null || mc.world == null) return;
        mc.player.networkHandler.onDisconnect(new DisconnectS2CPacket(Text.of("[AutoLog] current health are not support")));
    }

}
