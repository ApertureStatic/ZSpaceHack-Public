package dev.zspacehack.utils;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryUtil implements Wrapper {

    public static void doSwap(int slot) {
        mc.player.getInventory().selectedSlot = slot;
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
    }

    public static ItemStack getStackInSlot(int i) {
        return mc.player.getInventory().getStack(i);
    }
    public static int findItem(Item input) {
        for (int i = 0; i < 9; ++i) {
            Item item = getStackInSlot(i).getItem();
            if (Item.getRawId(item) != Item.getRawId(input)) continue;
            return i;
        }
        return -1;
    }

    public static int findClass(Class clazz) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = getStackInSlot(i);
            if (stack == ItemStack.EMPTY) continue;
            if (clazz.isInstance(stack.getItem())) {
                return i;
            }
            if (!clazz.isInstance(((BlockItem) stack.getItem()).getBlock()))
                continue;
            return i;
        }
        return -1;
    }

    public static int findBlock(Block blockIn) {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = getStackInSlot(i);
            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof BlockItem) || ((BlockItem) stack.getItem()).getBlock() != blockIn)
                continue;
            return i;
        }
        return -1;
    }

    public static int findUnBlock() {
        for (int i = 0; i < 9; ++i) {
            ItemStack stack = getStackInSlot(i);
            if (stack.getItem() instanceof BlockItem)
                continue;
            return i;
        }
        return -1;
    }

    public static int findItemInventorySlot(Item item, boolean offHand) {
        AtomicInteger slot = new AtomicInteger();
        slot.set(-1);
        for (Map.Entry<Integer, ItemStack> entry : getInventoryAndHotbarSlots().entrySet()) {
            if (entry.getValue().getItem() != item || entry.getKey() == 45 && !offHand) continue;
            slot.set(entry.getKey());
            return slot.get();
        }
        return slot.get();
    }

    public static Map<Integer, ItemStack> getInventoryAndHotbarSlots() {
        HashMap<Integer, ItemStack> fullInventorySlots = new HashMap<>();

        for (int current = 0; current <= 44; ++current) {
            fullInventorySlots.put(current, mc.player.getInventory().getStack(current));
        }

        return fullInventorySlots;
    }
}

