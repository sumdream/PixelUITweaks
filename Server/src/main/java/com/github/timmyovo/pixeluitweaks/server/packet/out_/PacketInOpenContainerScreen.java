package com.github.timmyovo.pixeluitweaks.server.packet.out_;

import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import com.github.timmyovo.pixeluitweaks.common.packet.PacketInTypes;
import com.github.timmyovo.pixeluitweaks.server.packet.IPacketOut;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class PacketInOpenContainerScreen implements IPacketOut {
    private int windowId;
    private ComponentContainer componentContainer;

    public PacketInOpenContainerScreen(int windowId, ComponentContainer componentContainer) {
        this.windowId = windowId;
        this.componentContainer = componentContainer;
    }

    @Override
    public void writePacket(PacketDataSerializer packetBuffer) {
        packetBuffer.writeInt(windowId);
        packetBuffer.a(GuiFactory.GSON.toJson(componentContainer));
    }

    @Override
    public String getPacketType() {
        return PacketInTypes.OpenContainerScreen.name();
    }
}
