package com.github.timmyovo.pixeluitweaks.server.container;

import net.minecraft.server.v1_12_R1.*;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryDoubleChest;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryView;
import org.bukkit.inventory.Inventory;

public class CommonContainer extends Container {
    private final IInventory container;
    private final int line;
    private CraftInventoryView bukkitEntity = null;
    private PlayerInventory player;

    public CommonContainer(IInventory playerInv, IInventory chestInv, EntityHuman entityhuman) {
        this.container = chestInv;
        this.line = chestInv.getSize() / 9;
        chestInv.startOpen(entityhuman);
        int i = (this.line - 4) * 18;
        this.player = (PlayerInventory) playerInv;

        int j;
        int k;
        for (j = 0; j < this.line; ++j) {
            for (k = 0; k < 9; ++k) {
                int i1 = k + j * 9;
                this.a(new Slot(chestInv, i1, 8 + k * 18, 18 + j * 18));
            }
        }

        for (j = 0; j < 3; ++j) {
            for (k = 0; k < 9; ++k) {
                this.a(new Slot(playerInv, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        for (j = 0; j < 9; ++j) {
            this.a(new Slot(playerInv, j, 8 + j * 18, 161 + i));
        }

    }

    public CraftInventoryView getBukkitView() {
        if (this.bukkitEntity != null) {
            return this.bukkitEntity;
        } else {
            Object inventory;
            if (this.container instanceof PlayerInventory) {
                inventory = new CraftInventoryPlayer((PlayerInventory) this.container);
            } else if (this.container instanceof InventoryLargeChest) {
                inventory = new CraftInventoryDoubleChest((InventoryLargeChest) this.container);
            } else {
                inventory = new CraftInventory(this.container);
            }

            this.bukkitEntity = new CraftInventoryView(this.player.player.getBukkitEntity(), (Inventory) inventory, this);
            return this.bukkitEntity;
        }
    }

    public boolean canUse(EntityHuman entityhuman) {
        return !this.checkReachable ? true : this.container.a(entityhuman);
    }

    public ItemStack shiftClick(EntityHuman entityhuman, int i) {
        ItemStack itemstack = ItemStack.a;
        Slot slot = (Slot) this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.cloneItemStack();
            if (i < this.line * 9) {
                if (!this.a(itemstack1, this.line * 9, this.slots.size(), true)) {
                    return ItemStack.a;
                }
            } else if (!this.a(itemstack1, 0, this.line * 9, false)) {
                return ItemStack.a;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.a);
            } else {
                slot.f();
            }
        }

        return itemstack;
    }

    public void b(EntityHuman entityhuman) {
        super.b(entityhuman);
        this.container.closeContainer(entityhuman);
    }

    public IInventory e() {
        return this.container;
    }
}
