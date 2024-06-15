package dev.zspacehack.utils;

public class MathUtil {
    public static float clamp(float num, float min, float max) {
        return num < min ? min : Math.min(num, max);
    }
}
