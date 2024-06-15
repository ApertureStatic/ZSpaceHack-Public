/**
 * Anti-Invis Module
 */
package dev.zspacehack.module.modules.combat;

import dev.zspacehack.module.Module;
import dev.zspacehack.settings.BooleanSetting;
import dev.zspacehack.settings.Setting;
import dev.zspacehack.utils.BlockUtil;
import dev.zspacehack.utils.InventoryUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Field;

public class Burrow extends Module {
	public static Burrow INSTANCE;
	private final BooleanSetting rotate =
			new BooleanSetting("Rotate", "burrow_rotate",true);
	public Burrow() {
		super("Burrow", Category.Combat);
		INSTANCE = this;
		try {
			for (Field field : Burrow.class.getDeclaredFields()) {
				if (!Setting.class.isAssignableFrom(field.getType()))
					continue;
				Setting setting = (Setting) field.get(this);
				addSetting(setting);
			}
		} catch (Exception e) {
		}
	}

	@Override
	public void onUpdate() {
		if (!mc.player.isOnGround()) {
			return;
		}
		int obsidian = InventoryUtil.findBlock(Blocks.OBSIDIAN);
		int eChest = InventoryUtil.findBlock(Blocks.ENDER_CHEST);
		int old = mc.player.getInventory().selectedSlot;
		if (obsidian == -1 && eChest == -1) {
			//CommandManager.sendChatMessage("Block?");
			disable();
			return;
		}
		BlockPos pos1 = new BlockPos(MathHelper.floor(mc.player.getX() + 0.2), MathHelper.floor(mc.player.getY() + 0.5), MathHelper.floor(mc.player.getZ() + 0.2));
		BlockPos pos2 = new BlockPos(MathHelper.floor(mc.player.getX() - 0.2), MathHelper.floor(mc.player.getY() + 0.5), MathHelper.floor(mc.player.getZ() + 0.2));
		BlockPos pos3 = new BlockPos(MathHelper.floor(mc.player.getX() + 0.2), MathHelper.floor(mc.player.getY() + 0.5), MathHelper.floor(mc.player.getZ() - 0.2));
		BlockPos pos4 = new BlockPos(MathHelper.floor(mc.player.getX() - 0.2), MathHelper.floor(mc.player.getY() + 0.5), MathHelper.floor(mc.player.getZ() - 0.2));
		if (!canPlace(pos1) && !canPlace(pos2) && !canPlace(pos3) && !canPlace(pos4)) {
			//CommandManager.sendChatMessage("Finish");
			disable();
			return;
		}
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.4199999868869781, mc.player.getZ(), false));
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.7531999805212017, mc.player.getZ(), false));
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 0.9999957640154541, mc.player.getZ(), false));
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 1.1661092609382138, mc.player.getZ(), false));
		InventoryUtil.doSwap(obsidian != -1 ? obsidian : eChest);
		if (canPlace(pos1)) {
			BlockUtil.placeBlock(pos1, rotate.getValue());
		} else if (canPlace(pos2)) {
			BlockUtil.placeBlock(pos2, rotate.getValue());
		} else if (canPlace(pos3)) {
			BlockUtil.placeBlock(pos3, rotate.getValue());
		} else if (canPlace(pos4)) {
			BlockUtil.placeBlock(pos4, rotate.getValue());
		}
		InventoryUtil.doSwap(old);
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() + 3.3400880035762786, mc.player.getZ(), false));
		mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(mc.player.getX(), mc.player.getY() - 1.0, mc.player.getZ(), false));
		double distance = 0;
		BlockPos bestPos = null;
		for (BlockPos pos : BlockUtil.getSphere(6)) {
			if (!canGoto(pos))
				continue;
			if (bestPos == null || mc.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < distance) {
				bestPos = pos;
				distance = mc.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
			}
		}
		if (bestPos != null) {
			mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(bestPos.getX() + 0.5, bestPos.getY(), bestPos.getZ() + 0.5, false));
		}
		disable();
	}
	
	private boolean canGoto(BlockPos pos) {
		return !BlockUtil.getState(pos).blocksMovement() && !BlockUtil.getState(pos.up()).blocksMovement();
	}

	private boolean canPlace(BlockPos pos) {
		if (!BlockUtil.canBlockFacing(pos)) {
			//CommandManager.sendChatMessage("facing false");
			return false;
		}
		if (!BlockUtil.canReplace(pos)) {
			//CommandManager.sendChatMessage("replace false");
			return false;
		}
		return !hasEntity(pos);
	}

	private boolean hasEntity(BlockPos pos) {
		for (Entity entity : mc.world.getNonSpectatingEntities(Entity.class, new Box(pos))) {
			if (entity == mc.player) continue;
			if (!entity.isAlive() || entity instanceof ItemEntity || entity instanceof ExperienceOrbEntity || entity instanceof ExperienceBottleEntity || entity instanceof ArrowEntity || entity instanceof EndCrystalEntity)
				continue;
			return true;
		}
		return false;
	}
}