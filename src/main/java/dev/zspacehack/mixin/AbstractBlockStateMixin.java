package dev.zspacehack.mixin;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.State;
import net.minecraft.state.property.Property;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBlockState.class)
public abstract class AbstractBlockStateMixin extends State<Block, BlockState> {

	private AbstractBlockStateMixin(Block object, ImmutableMap<Property<?>, Comparable<?>> immutableMap, MapCodec<BlockState> mapCodec)
	{
		super(object, immutableMap, mapCodec);
	}
}
