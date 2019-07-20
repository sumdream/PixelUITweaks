package com.github.timmyovo.pixeluitweaks.server.inventory;

import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentSlot;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryDoubleChest;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class ServerDummyContainer extends Container {
    private CraftInventoryView bukkitEntity = null;
    private EntityPlayer entityPlayer;
    private IInventory inventory;

    public ServerDummyContainer(EntityPlayer entityPlayer, IInventory inventory, List<ComponentSlot> componentSlotList) {
        this.entityPlayer = entityPlayer;
        this.inventory = inventory;
        bindPlayerInventory(entityPlayer.inventory);
        componentSlotList.stream()
                .map(this::toMinecraftSlot)
                .forEach(this::a);
    }

    private static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        return stackB.getItem() == stackA.getItem() && (!stackA.usesData() || stackA.getData() == stackB.getData()) && ItemStack.equals(stackA, stackB);
    }

    public Slot toMinecraftSlot(ComponentSlot componentSlot) {
        return new Slot(inventory, componentSlot.getSlotIndex(), componentSlot.getSlotX(), componentSlot.getSlotY());
    }

    protected void bindPlayerInventory(PlayerInventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.a(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.a(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    public CraftInventoryView getBukkitView() {
        if (this.bukkitEntity != null) {
            return this.bukkitEntity;
        } else {
            Object inventory;
            if (this.inventory instanceof PlayerInventory) {
                inventory = new CraftInventoryPlayer((PlayerInventory) this.inventory);
            } else if (this.inventory instanceof InventoryLargeChest) {
                inventory = new CraftInventoryDoubleChest((InventoryLargeChest) this.inventory);
            } else {
                inventory = new CraftInventory(this.inventory);
            }

            this.bukkitEntity = new CraftInventoryView(this.entityPlayer.getBukkitEntity(), (Inventory) inventory, this);
            return this.bukkitEntity;
        }
    }

    @Override
    public ItemStack shiftClick(EntityHuman entityhuman, int index) {
        ItemStack itemstack = ItemStack.a;
        Slot slot = (Slot) this.slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.cloneItemStack();

            if (index < slots.size()) {
                if (!this.a(itemstack1, slots.size(), this.slots.size(), true)) {
                    return ItemStack.a;
                }
            } else if (!this.a(itemstack1, 0, slots.size(), false)) {
                return ItemStack.a;
            }

            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.a);
            } else {
                slot.f();
            }
        }

        return itemstack;
    }

    @Override
    protected boolean a(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) i = endIndex - 1;

        if (stack.isStackable()) {
            while (stack.getCount() > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
                Slot slot = (Slot) this.slots.get(i);
                ItemStack itemstack = slot.getItem();
                int maxLimit = Math.min(stack.getMaxStackSize(), slot.getMaxStackSize());

                if (!itemstack.isEmpty() && areItemStacksEqual(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    if (j <= maxLimit) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.f();
                        flag = true;

                    } else if (itemstack.getCount() < maxLimit) {
                        stack.subtract(maxLimit - itemstack.getCount());
                        itemstack.setCount(maxLimit);
                        slot.f();
                        flag = true;
                    }
                }
                if (reverseDirection) {
                    --i;
                } else ++i;
            }
        }
        if (stack.getCount() > 0) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else i = startIndex;

            while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex) {
                Slot slot1 = (Slot) this.slots.get(i);
                ItemStack itemstack1 = slot1.getItem();

                if (itemstack1.isEmpty() && slot1.isAllowed(stack)) { // Forge: Make sure to respect isItemValid in the slot.
                    if (stack.getCount() <= slot1.getMaxStackSize()) {
                        slot1.set(stack.cloneItemStack());
                        slot1.f();
                        stack.setCount(0);
                        flag = true;
                        break;
                    } else {
                        itemstack1 = stack.cloneItemStack();
                        stack.subtract(slot1.getMaxStackSize());
                        itemstack1.setCount(slot1.getMaxStackSize());
                        slot1.set(itemstack1);
                        slot1.f();
                        flag = true;
                    }
                }
                if (reverseDirection) {
                    --i;
                } else ++i;
            }
        }
        return flag;
    }

    @Override
    public boolean canUse(EntityHuman entityHuman) {
        return true;
    }
}
