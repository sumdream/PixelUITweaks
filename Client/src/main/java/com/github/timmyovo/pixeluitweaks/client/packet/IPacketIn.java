package com.github.timmyovo.pixeluitweaks.client.packet;

import net.minecraft.network.PacketBuffer;

public interface IPacketIn {
    void readPacket(PacketBuffer packetBuffer);
}
