package dev.zspacehack.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.FluidDrainable;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FluidBlock.class)
public abstract class FluidBlockMixin extends Block implements FluidDrainable {
	private FluidBlockMixin(Settings blockSettings) {
		super(blockSettings);
	}
}
