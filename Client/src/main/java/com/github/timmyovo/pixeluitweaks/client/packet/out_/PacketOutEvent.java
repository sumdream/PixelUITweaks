package com.github.timmyovo.pixeluitweaks.client.packet.out_;

import com.github.timmyovo.pixeluitweaks.client.packet.IPacketOut;
import com.github.timmyovo.pixeluitweaks.common.api.ISerializable;
import com.github.timmyovo.pixeluitweaks.common.event.GuiEventType;
import net.minecraft.network.PacketBuffer;

public class PacketOutEvent implements IPacketOut {
    private GuiEventType guiEventType;
    private ISerializable eventModel;

    public PacketOutEvent(GuiEventType guiEventType, ISerializable eventModel) {
        this.guiEventType = guiEventType;
        this.eventModel = eventModel;
    }

    public static void notifyEvent(GuiEventType guiEventType, ISerializable iSerializable) {
        PacketOutEvent packetOutEvent = new PacketOutEvent(guiEventType, iSerializable);
        packetOutEvent.sendPacket();
    }

    @Override
    public void writePacket(PacketBuffer packetBuffer) {
        packetBuffer.writeString(guiEventType.name());
        packetBuffer.writeString(eventModel.asString());
    }
}
