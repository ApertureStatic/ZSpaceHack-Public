package dev.zspacehack.ZSpaceITRMTD;

import dev.zspacehack.utils.BlockUtil;
import net.minecraft.util.math.BlockPos;

public class client_ITRMTD {
    public void client_itr_place_mtd(BlockPos blockPos){
        BlockUtil.placeBlock(blockPos,false);
    }
}
