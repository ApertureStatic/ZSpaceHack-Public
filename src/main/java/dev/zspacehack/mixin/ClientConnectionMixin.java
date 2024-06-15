package dev.zspacehack.mixin;

import dev.zspacehack.ZSpace;
import dev.zspacehack.events.impl.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {

	@Inject(at = { @At(value = "INVOKE", target = "Lnet/minecraft/network/ClientConnection;handlePacket(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;)V", ordinal = 0) }, method = "channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V", cancellable = true)
	protected void onChannelRead(ChannelHandlerContext channelHandlerContext, Packet<?> packet, CallbackInfo ci) {
		PacketEvent event = new PacketEvent.Receive(packet);
		ZSpace.EVENT_BUS.post(event);
		if (event.isCancel()) {
			ci.cancel();
		}
	}

	@Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"),cancellable = true)
	private void onSendPacketPre(Packet<?> packet, CallbackInfo info) {
		PacketEvent.Send event = new PacketEvent.Send(packet);
		ZSpace.EVENT_BUS.post(event);
		if (event.isCancel()) {
			info.cancel();
		}
	}
}
