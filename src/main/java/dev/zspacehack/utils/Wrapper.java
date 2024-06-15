package dev.zspacehack.utils;

import dev.zspacehack.interfaces.IMinecraftClient;
import net.minecraft.client.MinecraftClient;

public interface Wrapper {
    MinecraftClient mc = MinecraftClient.getInstance();
    IMinecraftClient imc = (IMinecraftClient) mc;
}
