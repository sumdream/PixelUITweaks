package com.github.timmyovo.pixeluitweaks.server.container;

import net.minecraft.server.v1_12_R1.InventorySubcontainer;
import org.bukkit.inventory.InventoryHolder;

public class CommonInventory extends InventorySubcontainer {
    public CommonInventory(String s, boolean flag, int i) {
        super(s, flag, i);
    }

    public CommonInventory(String s, boolean flag, int i, InventoryHolder owner) {
        super(s, flag, i, owner);
    }
}
