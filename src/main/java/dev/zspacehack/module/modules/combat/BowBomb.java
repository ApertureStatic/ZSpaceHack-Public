package dev.zspacehack.module.modules.combat;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.SliderSetting;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

public class BowBomb extends Module {
    private SliderSetting amountProperty = new SliderSetting("Amount", 100, 10, 5000, 1);
    public BowBomb() {
        super("BowBomb", Category.Combat);
        addSetting(amountProperty);
    }
    @Override
    public void onUpdate() {
        if (mc.player == null || mc.world == null) return;
        if (mc.player.getMainHandStack().getItem() == Items.BOW) {
            mc.player.networkHandler.sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_SPRINTING));
            for (int i = 0; i < amountProperty.getValueFloat(); i++) {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 1.0E-9, mc.player.getZ(), true));
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.0E-9, mc.player.getZ(), false));
            }
        }
    }
}