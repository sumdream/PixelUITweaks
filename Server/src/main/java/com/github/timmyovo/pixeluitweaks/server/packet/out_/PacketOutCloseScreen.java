package com.github.timmyovo.pixeluitweaks.server.packet.out_;


import com.github.timmyovo.pixeluitweaks.common.packet.PacketTypes;
import com.github.timmyovo.pixeluitweaks.server.packet.IPacketOut;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

import java.util.Random;

public class PacketOutCloseScreen implements IPacketOut {
    @Override
    public void writePacket(PacketDataSerializer packetBuffer) {
        packetBuffer.writeInt(new Random().nextInt(1000));
    }

    @Override
    public String getPacketType() {
        return PacketTypes.CloseScreen.name();
    }
}
