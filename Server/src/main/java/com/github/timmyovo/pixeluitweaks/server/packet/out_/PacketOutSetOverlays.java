package com.github.timmyovo.pixeluitweaks.server.packet.out_;

import com.github.timmyovo.pixeluitweaks.common.gui.InGameOverlays;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import com.github.timmyovo.pixeluitweaks.common.packet.PacketTypes;
import com.github.timmyovo.pixeluitweaks.server.packet.IPacketOut;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

public class PacketOutSetOverlays implements IPacketOut {
    private InGameOverlays inGameOverlays;

    public PacketOutSetOverlays(InGameOverlays inGameOverlays) {
        this.inGameOverlays = inGameOverlays;
    }

    @Override
    public void writePacket(PacketDataSerializer packetBuffer) {
        packetBuffer.a(GuiFactory.GSON.toJson(inGameOverlays));
    }

    @Override
    public String getPacketType() {
        return PacketTypes.UpdateGameOverlay.name();
    }
}
