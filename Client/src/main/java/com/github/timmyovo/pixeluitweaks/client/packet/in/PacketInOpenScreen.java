package com.github.timmyovo.pixeluitweaks.client.packet.in;

import com.github.timmyovo.pixeluitweaks.client.packet.IPacketIn;
import com.github.timmyovo.pixeluitweaks.client.packet.manager.LocalDataManager;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;

public class PacketInOpenScreen implements IPacketIn {
    @Override
    public void readPacket(PacketBuffer packetBuffer) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            String containerString = packetBuffer.readString(Short.MAX_VALUE);
            ComponentContainer componentContainer = GuiFactory.fromString(containerString, ComponentContainer.class);
            LocalDataManager.getCommonComponentContainers().add(componentContainer);
//            Minecraft minecraft = Minecraft.getMinecraft();
//            EntityPlayerSP player = minecraft.player;
//            CommonUIScreen slotUIScreen = new CommonUIScreen(player, componentContainer);
//            minecraft.displayGuiScreen(slotUIScreen);
        });

    }
}
