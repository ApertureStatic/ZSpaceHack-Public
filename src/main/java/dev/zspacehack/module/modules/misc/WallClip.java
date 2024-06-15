package dev.zspacehack.module.modules.misc;

import dev.zspacehack.module.Module;

public class WallClip extends Module {
    public static WallClip INSTANCE;
    public WallClip(){
        super("WallClip", Category.Misc);
        INSTANCE = this;
    }
}
