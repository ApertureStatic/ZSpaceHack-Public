package dev.zspacehack.settings;

import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Field;

public class BindSetting extends Setting {
    private boolean isListening;
    private int key;

    public BindSetting(String name, String line) {
        super(name, line);
        this.key = -1;
    }

    public BindSetting(String name, String line, int key) {
        super(name, line);
        this.key = key;
    }

    @Override
    public void loadSetting() {

    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getBind() {
        if (key == -1) return "None";
        String kn = this.key > 0 ? GLFW.glfwGetKeyName(this.key, GLFW.glfwGetKeyScancode(this.key)) : "None";
        if (kn == null) {
            try {
                for (Field declaredField : GLFW.class.getDeclaredFields()) {
                    if (declaredField.getName().startsWith("GLFW_KEY_")) {
                        int a = (int) declaredField.get(null);
                        if (a == this.key) {
                            String nb = declaredField.getName().substring("GLFW_KEY_".length());
                            kn = nb.substring(0, 1).toUpperCase() + nb.substring(1).toLowerCase();
                        }
                    }
                }
            } catch (Exception ignored) {
                kn = "None";
            }
        }

        return (kn + "").toUpperCase();
    }

    public void setListening(boolean set) {
        isListening = set;
    }

    public boolean isListening() {
        return isListening;
    }
}
