package com.github.timmyovo.pixeluitweaks.server.packet;


import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;

public interface IPacketOut {
    void writePacket(PacketDataSerializer packetBuffer);

    String getPacketType();

    default void sendPacket(EntityPlayer entityPlayer) {
        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(Unpooled.buffer());
        writePacket(packetDataSerializer);
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutCustomPayload(getPacketType(), packetDataSerializer));
    }
}
