package dev.zspacehack.mixin;

import dev.zspacehack.module.modules.combat.FakeDmg;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class DamageManagerMixin {
    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    public void isInvulnerableToMixin(DamageSource damageSource,CallbackInfoReturnable<Boolean> cir) {
        if (FakeDmg.INSTANCE.isOn()){
            cir.setReturnValue(true);
        }
    }
}
