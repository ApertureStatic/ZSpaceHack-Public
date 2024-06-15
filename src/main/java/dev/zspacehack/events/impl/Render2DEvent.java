package dev.zspacehack.events.impl;

import dev.zspacehack.events.Event;
import net.minecraft.client.gui.DrawContext;

public class Render2DEvent extends Event {
    public Render2DEvent(DrawContext context) {
        super(Stage.Pre);
    }
}
