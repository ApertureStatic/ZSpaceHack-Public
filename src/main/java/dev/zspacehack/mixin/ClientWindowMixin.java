package dev.zspacehack.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
@Mixin(MinecraftClient.class)
public class ClientWindowMixin {
    
        @Inject(method = "getWindowTitle",at = @At("HEAD"),cancellable = true)
        private void getWindowTitle(CallbackInfoReturnable<String> stringCallbackInfoReturnable)
        {
            stringCallbackInfoReturnable.setReturnValue("\uD835\uDC9B\uD835\uDC94\uD835\uDC91\uD835\uDC82\uD835\uDC84\uD835\uDC86-\uD835\uDC89\uD835\uDC82\uD835\uDC84\uD835\uDC8C");
        }

    }

