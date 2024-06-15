package dev.zspacehack.events.impl;

import dev.zspacehack.events.Event;
import net.minecraft.client.util.math.MatrixStack;

public class Render3DEvent extends Event {
    private final MatrixStack matrixStack;
    public Render3DEvent(MatrixStack matrixStack) {
        super(Stage.Pre);
        this.matrixStack = matrixStack;
    }
    public MatrixStack getMatrixStack() {
        return this.matrixStack;
    }
}
