package com.github.timmyovo.pixeluitweaks.client.packet.in;

import com.github.timmyovo.pixeluitweaks.client.gui.CommonUIScreen;
import com.github.timmyovo.pixeluitweaks.client.packet.IPacketIn;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.PacketBuffer;

public class PacketInOpenScreen implements IPacketIn {
    @Override
    public void readPacket(PacketBuffer packetBuffer) {
        String containerString = packetBuffer.readString(Short.MAX_VALUE);
        ComponentContainer componentContainer = GuiFactory.fromString(containerString, ComponentContainer.class);
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        CommonUIScreen slotUIScreen = new CommonUIScreen(player, componentContainer);
        Minecraft.getMinecraft().displayGuiScreen(slotUIScreen);
    }
}