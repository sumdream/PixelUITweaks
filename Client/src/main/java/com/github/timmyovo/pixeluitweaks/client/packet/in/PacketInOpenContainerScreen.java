package com.github.timmyovo.pixeluitweaks.client.packet.in;

import com.github.timmyovo.pixeluitweaks.client.packet.IPacketIn;
import com.github.timmyovo.pixeluitweaks.client.packet.manager.LocalDataManager;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;

public class PacketInOpenContainerScreen implements IPacketIn {
    @Override
    public void readPacket(PacketBuffer packetBuffer) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            //实现已被删除,因为不知道客户端缺少了什么神奇的操作所以用forge的服务端包代替打开
            int windowId = packetBuffer.readInt();
            String containerString = packetBuffer.readString(Short.MAX_VALUE);
            ComponentContainer componentContainer = GuiFactory.fromString(containerString, ComponentContainer.class);
            LocalDataManager.getSlotComponentContainers().add(componentContainer);
////            EntityPlayerSP player = Minecraft.getMinecraft().player;
////            SlotUIScreen slotUIScreen = new SlotUIScreen(player, componentContainer);
////            Minecraft.getMinecraft().displayGuiScreen(slotUIScreen);
////            player.openContainer.windowId = windowId;
        });
    }
}
