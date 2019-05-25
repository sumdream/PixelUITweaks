package com.github.timmyovo.pixeluitweaks.client.packet.in;

import com.github.timmyovo.pixeluitweaks.client.hook.SidebarManager;
import com.github.timmyovo.pixeluitweaks.client.packet.IPacketIn;
import com.github.timmyovo.pixeluitweaks.common.gui.sidebar.Sidebar;
import com.github.timmyovo.pixeluitweaks.common.gui.sidebar.SidebarType;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import net.minecraft.network.PacketBuffer;

public class PacketUpdateOverlayListContent implements IPacketIn {
    @Override
    public void readPacket(PacketBuffer packetBuffer) {
        String readString = packetBuffer.readString(Short.MAX_VALUE);
        Sidebar sidebar = GuiFactory.fromString(readString, Sidebar.class);
        if (sidebar.getSidebarType() == SidebarType.ADD) {
            SidebarManager.addSidebar(sidebar);
            return;
        }
        if (sidebar.getSidebarType() == SidebarType.REMOVE) {
            SidebarManager.removeSidebar(sidebar);
        }
    }
}
