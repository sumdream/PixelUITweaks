package com.github.timmyovo.pixeluitweaks.server.packet.out_;

import com.github.timmyovo.pixeluitweaks.common.gui.sidebar.Sidebar;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import com.github.timmyovo.pixeluitweaks.common.packet.PacketInTypes;
import com.github.timmyovo.pixeluitweaks.server.packet.IPacketOut;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class PacketOutListContent implements IPacketOut {
    private Sidebar sidebar;

    public PacketOutListContent(Sidebar sidebar) {
        this.sidebar = sidebar;
    }

    @Override
    public void writePacket(PacketDataSerializer packetBuffer) {
        packetBuffer.a(GuiFactory.GSON.toJson(sidebar));
    }

    @Override
    public String getPacketType() {
        return PacketInTypes.UpdateOverlayContent.name();
    }
}
