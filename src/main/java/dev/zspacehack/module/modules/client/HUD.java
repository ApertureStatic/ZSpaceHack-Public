package dev.zspacehack.module.modules.client;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.SliderSetting;

public class HUD extends Module {
    public static HUD INSTANCE;
    public SliderSetting hudY1 = new SliderSetting("HUD Y1", 0, 0, 300, 1);
    public SliderSetting hudY2 = new SliderSetting("HUD Y2", 20, 0, 300, 1);
    public SliderSetting hudY3 = new SliderSetting("HUD Y3", 40, 0, 300, 1);
    public HUD() {
        super("HUD",Category.Client);
        INSTANCE = this;
        addSetting(hudY1);
        addSetting(hudY2);
        addSetting(hudY3);
    }


}
