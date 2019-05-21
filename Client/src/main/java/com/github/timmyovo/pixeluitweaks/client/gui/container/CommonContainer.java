package com.github.timmyovo.pixeluitweaks.client.gui.container;

import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class CommonContainer extends Container {
    protected List<ComponentSlot> slots;
    private EntityPlayer entityPlayer;

    public CommonContainer(List<ComponentSlot> slots, EntityPlayer entityPlayer) {
        this.slots = slots;
        this.entityPlayer = entityPlayer;
        bindPlayerInventory(entityPlayer.inventory);
        slots.stream()
                .map(this::toMinecraftSlot)
                .forEach(this::addSlotToContainer);
    }

    private static boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB) {
        return stackB.getItem() == stackA.getItem() && (!stackA.getHasSubtypes() || stackA.getMetadata() == stackB.getMetadata()) && ItemStack.areItemStackTagsEqual(stackA, stackB);
    }

    public Slot toMinecraftSlot(ComponentSlot componentSlot) {
        return new Slot(entityPlayer.inventory, componentSlot.getSlotIndex(), componentSlot.getXPos(), componentSlot.getYPos());
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    protected void bindPlayerInventory(InventoryPlayer playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (int k = 0; k < 9; ++k) {
            this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < slots.size()) {
                if (!this.mergeItemStack(itemstack1, slots.size(), this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, slots.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) i = endIndex - 1;

        if (stack.isStackable()) {
            while (stack.getCount() > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
                Slot slot = (Slot) this.inventorySlots.get(i);
                ItemStack itemstack = slot.getStack();
                int maxLimit = Math.min(stack.getMaxStackSize(), slot.getSlotStackLimit());

                if (!itemstack.isEmpty() && areItemStacksEqual(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();
                    if (j <= maxLimit) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot.onSlotChanged();
                        flag = true;

                    } else if (itemstack.getCount() < maxLimit) {
                        stack.shrink(maxLimit - itemstack.getCount());
                        itemstack.setCount(maxLimit);
                        slot.onSlotChanged();
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
                Slot slot1 = (Slot) this.inventorySlots.get(i);
                ItemStack itemstack1 = slot1.getStack();

                if (itemstack1.isEmpty() && slot1.isItemValid(stack)) { // Forge: Make sure to respect isItemValid in the slot.
                    if (stack.getCount() <= slot1.getSlotStackLimit()) {
                        slot1.putStack(stack.copy());
                        slot1.onSlotChanged();
                        stack.setCount(0);
                        flag = true;
                        break;
                    } else {
                        itemstack1 = stack.copy();
                        stack.shrink(slot1.getSlotStackLimit());
                        itemstack1.setCount(slot1.getSlotStackLimit());
                        slot1.putStack(itemstack1);
                        slot1.onSlotChanged();
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
}