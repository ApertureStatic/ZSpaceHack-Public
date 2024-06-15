/**
 * A helper class that contains several useful functions.
 */
package dev.zspacehack.utils;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.*;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;

import java.util.Objects;
import java.util.stream.Stream;

public class ModuleUtils implements Wrapper{

	public static boolean isThrowable(ItemStack stack) {
		Item item = stack.getItem();
		return item instanceof BowItem || item instanceof SnowballItem || item instanceof EggItem
				|| item instanceof EnderPearlItem || item instanceof SplashPotionItem
				|| item instanceof LingeringPotionItem || item instanceof FishingRodItem;
	}
	
	public static double throwableGravity(Item item) {
		if(item instanceof RangedWeaponItem) {
			return 0.05;
		}else if(item instanceof ThrowablePotionItem) {
			return 0.4;
		}else if(item instanceof FishingRodItem) {
			return 0.15;
		}else if(item instanceof TridentItem) {
			return 0.015;
		}else {
			return 0.03;
		}
	}
	
	public static boolean isPlantable(ItemStack stack) {
		Item item = stack.getItem();
		return item == Items.WHEAT_SEEDS ||  item == Items.CARROT || item == Items.POTATO;
	}
	
	public static Stream<BlockEntity> getTileEntities(){
		return getLoadedChunks().flatMap(chunk -> chunk.getBlockEntities().values().stream());
	}
	
	public static Stream<WorldChunk> getLoadedChunks(){
		int radius = Math.max(2, mc.options.getClampedViewDistance()) + 3;
		int diameter = radius * 2 + 1;
		
		ChunkPos center = mc.player.getChunkPos();
		ChunkPos min = new ChunkPos(center.x - radius, center.z - radius);
		ChunkPos max = new ChunkPos(center.x + radius, center.z + radius);
		
		Stream<WorldChunk> stream = Stream.<ChunkPos> iterate(min, pos -> {
			int x = pos.x;
			int z = pos.z;
			x++;
			
			if(x > max.x)
			{
				x = min.x;
				z++;
			}
			
			return new ChunkPos(x, z);

		}).limit(diameter*diameter)
			.filter(c -> mc.world.isChunkLoaded(c.x, c.z))
			.map(c -> mc.world.getChunk(c.x, c.z)).filter(Objects::nonNull);
		
		return stream;
	}
}
