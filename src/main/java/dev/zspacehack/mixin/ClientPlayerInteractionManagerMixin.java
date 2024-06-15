package dev.zspacehack.mixin;

import dev.zspacehack.module.modules.combat.Reach;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
	@Inject(at = { @At("HEAD") }, method = { "getReachDistance()F" }, cancellable = true)
	private void onGetReachDistance(CallbackInfoReturnable<Float> ci) {
		Reach reachHack = Reach.INSTANCE;
		if (reachHack.isOn()) {
			ci.setReturnValue(reachHack.getReach());
		}
	}

	@Inject(at = { @At("HEAD") }, method = { "hasExtendedReach()Z" }, cancellable = true)
	private void hasExtendedReach(CallbackInfoReturnable<Boolean> cir) {
		Reach reachHack = Reach.INSTANCE;
		if (reachHack.isOn())
			cir.setReturnValue(true);
	}
}
