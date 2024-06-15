package dev.zspacehack.module.modules.combat;

import dev.zspacehack.module.Module;

public class FakeDmg extends Module {
    public static FakeDmg INSTANCE;
    public FakeDmg(){
        super("FakeDmg",Category.Combat);
        INSTANCE = this;
    }
}
