package com.github.timmyovo.pixeluitweaks.server.packet.out_;

import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import com.github.timmyovo.pixeluitweaks.common.packet.PacketTypes;
import com.github.timmyovo.pixeluitweaks.server.packet.IPacketOut;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class PacketOutRemoveContainer implements IPacketOut {
    private ComponentContainer componentContainer;

    public PacketOutRemoveContainer(ComponentContainer componentContainer) {
        this.componentContainer = componentContainer;
    }

    @Override
    public void writePacket(PacketDataSerializer packetBuffer) {
        String json = GuiFactory.GSON.toJson(componentContainer);
        packetBuffer.a(json);
    }

    @Override
    public String getPacketType() {
        return PacketTypes.RemoveContainer.name();
    }
}
