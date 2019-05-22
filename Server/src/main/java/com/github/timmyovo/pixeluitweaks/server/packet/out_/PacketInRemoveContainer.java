package com.github.timmyovo.pixeluitweaks.server.packet.out_;

import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import com.github.timmyovo.pixeluitweaks.common.packet.PacketInTypes;
import com.github.timmyovo.pixeluitweaks.server.packet.IPacketOut;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class PacketInRemoveContainer implements IPacketOut {
    private ComponentContainer componentContainer;

    public PacketInRemoveContainer(ComponentContainer componentContainer) {
        this.componentContainer = componentContainer;
    }

    @Override
    public void writePacket(PacketDataSerializer packetBuffer) {
        String json = GuiFactory.GSON.toJson(componentContainer);
        packetBuffer.a(json);
    }

    @Override
    public String getPacketType() {
        return PacketInTypes.PacketInRemoveContainer.name();
    }
}
