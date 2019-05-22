package com.github.timmyovo.pixeluitweaks.client.packet.in;

import com.github.timmyovo.pixeluitweaks.client.gui.CommonUIScreen;
import com.github.timmyovo.pixeluitweaks.client.gui.SlotUIScreen;
import com.github.timmyovo.pixeluitweaks.client.packet.IPacketIn;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.PacketBuffer;

public class PacketInAddContainer implements IPacketIn {
    @Override
    public void readPacket(PacketBuffer packetBuffer) {
        String s = packetBuffer.readString(Short.MAX_VALUE);
        ComponentContainer componentContainer = GuiFactory.fromString(s, ComponentContainer.class);
        GuiScreen currentScreen = Minecraft.getMinecraft().currentScreen;
        if (currentScreen instanceof SlotUIScreen) {
            ((SlotUIScreen) currentScreen).addContainer(componentContainer);
        }
        if (currentScreen instanceof CommonUIScreen) {
            ((CommonUIScreen) currentScreen).addContainer(componentContainer);
        }
    }
}
