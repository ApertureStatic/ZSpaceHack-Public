package dev.zspacehack.mixin;

import com.mojang.authlib.GameProfile;
import dev.zspacehack.ZSpace;
import dev.zspacehack.module.modules.movement.NoSlowdown;
import dev.zspacehack.module.modules.movement.Velocity;
import dev.zspacehack.events.Event;
import dev.zspacehack.events.impl.MoveEvent;
import dev.zspacehack.events.impl.UpdateWalkingEvent;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.MovementType;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {

	public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(method = "pushOutOfBlocks", at = @At("HEAD"), cancellable = true)
	private void onPushOutOfBlocksHook(double x, double d, CallbackInfo info) {
		if (Velocity.INSTANCE.isOn() && Velocity.INSTANCE.blockPush.getValue()) {
			info.cancel();
		}
	}

	@Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"), require = 0)
	private boolean tickMovementHook(ClientPlayerEntity player) {
		if (NoSlowdown.INSTANCE.isOn())
			return false;
		return player.isUsingItem();
	}

	@Inject(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;move(Lnet/minecraft/entity/MovementType;Lnet/minecraft/util/math/Vec3d;)V"), cancellable = true)
	public void onMoveHook(MovementType movementType, Vec3d movement, CallbackInfo ci) {
		MoveEvent event = new MoveEvent(movement.x, movement.y, movement.z);
		ZSpace.EVENT_BUS.post(event);
		ci.cancel();
		if (!event.isCancel()) {
			super.move(movementType, new Vec3d(event.getX(), event.getY(), event.getZ()));
		}
	}

	@Inject(method = {"sendMovementPackets"}, at = {@At(value = "HEAD")})
	private void preMotion(CallbackInfo info) {
		UpdateWalkingEvent event = new UpdateWalkingEvent(Event.Stage.Pre);
		ZSpace.EVENT_BUS.post(event);
	}

	@Inject(method = {"sendMovementPackets"}, at = {@At(value = "RETURN")})
	private void postMotion(CallbackInfo info) {
		UpdateWalkingEvent event = new UpdateWalkingEvent(Event.Stage.Post);
		ZSpace.EVENT_BUS.post(event);
	}

}
