package com.github.timmyovo.pixeluitweaks.client.packet.in;

import com.github.timmyovo.pixeluitweaks.client.packet.IPacketIn;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;

public class PacketInCloseScreen implements IPacketIn {
    @Override
    public void readPacket(PacketBuffer packetBuffer) {
        Minecraft.getMinecraft().displayGuiScreen(null);
    }
}
