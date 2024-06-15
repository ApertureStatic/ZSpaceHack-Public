package dev.zspacehack.settings;

import dev.zspacehack.module.ModuleManager;

public class EnumSetting extends Setting {
    private Enum value;
    public EnumSetting(String name, String line, Enum defaultValue) {
        super(name, line);
        value = defaultValue;
        loadSetting();
    }
    public EnumSetting(String name, Enum defaultValue) {
        super(name, ModuleManager.lastLoadModule.getName().toLowerCase() + "_" + name.toLowerCase());
        value = defaultValue;
        loadSetting();
    }
    public void increaseEnum() {
        value = EnumConverter.increaseEnum(value);
    }

    public final Enum getValue() {
        return this.value;
    }

    @Override
    public void loadSetting() {
        try {
            EnumConverter converter = new EnumConverter(value.getClass());
            Enum value = converter.doBackward(Settings.getSettingString(this.getLine()));
            if (value != null) {
                this.value = value;
            }
        } catch (Exception ignored) {

        }
    }
}
