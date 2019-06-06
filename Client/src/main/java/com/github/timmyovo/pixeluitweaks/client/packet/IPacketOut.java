package com.github.timmyovo.pixeluitweaks.client.packet;

import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;

public interface IPacketOut {
    String CHANNEL = "UT|EVENT";
    void writePacket(PacketBuffer packetBuffer);

    default void sendPacket() {
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        writePacket(packetBuffer);
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) {
            return;
        }
        NetHandlerPlayClient connection = player.connection;
        if (connection == null) {
            return;
        }
        connection.sendPacket(new CPacketCustomPayload(CHANNEL, packetBuffer));
    }
}
