package com.github.timmyovo.pixeluitweaks.client.packet.out_;

import com.github.timmyovo.pixeluitweaks.client.packet.IPacketOut;
import com.github.timmyovo.pixeluitweaks.common.api.ISerializable;
import com.github.timmyovo.pixeluitweaks.common.event.GuiEvents;
import net.minecraft.network.PacketBuffer;

public class PacketOutEvent implements IPacketOut {
    private GuiEvents guiEvents;
    private ISerializable eventModel;

    public PacketOutEvent(GuiEvents guiEvents, ISerializable eventModel) {
        this.guiEvents = guiEvents;
        this.eventModel = eventModel;
    }

    public static void notifyEvent(GuiEvents guiEvents, ISerializable iSerializable) {
        PacketOutEvent packetOutEvent = new PacketOutEvent(guiEvents, iSerializable);
        packetOutEvent.sendPacket();
    }

    @Override
    public void writePacket(PacketBuffer packetBuffer) {
        packetBuffer.writeString(guiEvents.name());
        packetBuffer.writeString(eventModel.asString());
    }
}
