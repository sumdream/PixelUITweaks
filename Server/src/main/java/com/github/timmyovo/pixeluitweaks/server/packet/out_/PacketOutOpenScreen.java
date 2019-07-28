package com.github.timmyovo.pixeluitweaks.server.packet.out_;

import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import com.github.timmyovo.pixeluitweaks.common.packet.PacketTypes;
import com.github.timmyovo.pixeluitweaks.server.packet.IPacketOut;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class PacketOutOpenScreen implements IPacketOut {
    private ComponentContainer componentContainer;

    public PacketOutOpenScreen(ComponentContainer componentContainer) {
        this.componentContainer = componentContainer;
    }

    @Override
    public void writePacket(PacketDataSerializer packetBuffer) {
        packetBuffer.a(GuiFactory.GSON.toJson(componentContainer));
    }

    @Override
    public String getPacketType() {
        return PacketTypes.OpenScreen.name();
    }
}
