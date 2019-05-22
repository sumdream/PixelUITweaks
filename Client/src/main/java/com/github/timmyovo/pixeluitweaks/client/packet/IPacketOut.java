package com.github.timmyovo.pixeluitweaks.client.packet;

import net.minecraft.network.PacketBuffer;

public interface IPacketOut {
    void writePacket(PacketBuffer packetBuffer);
}
