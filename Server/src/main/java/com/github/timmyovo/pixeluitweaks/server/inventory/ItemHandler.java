package com.github.timmyovo.pixeluitweaks.server.inventory;

import net.minecraft.server.v1_12_R1.InventorySubcontainer;

public class ItemHandler extends InventorySubcontainer {
    public ItemHandler(int slotCount) {
        super("", false, slotCount);
    }
}
