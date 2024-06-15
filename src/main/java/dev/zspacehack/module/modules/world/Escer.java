package dev.zspacehack.module.modules.world;

import dev.zspacehack.module.Module;

public class Escer extends Module {
    public Escer(){
        super("Escer",Category.World);
    }
    @Override
    public void onEnable() {
        if (nullCheck()) return;
        mc.player.networkHandler.sendChatCommand("kill");
    }
}
