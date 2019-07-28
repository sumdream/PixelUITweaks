package com.github.timmyovo.pixeluitweaks.client.packet.in;

import com.github.timmyovo.pixeluitweaks.client.packet.IPacketIn;
import com.github.timmyovo.pixeluitweaks.client.packet.manager.LocalDataManager;
import com.github.timmyovo.pixeluitweaks.common.gui.InGameOverlays;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import net.minecraft.network.PacketBuffer;

public class PacketInSetOverlays implements IPacketIn {
    @Override
    public void readPacket(PacketBuffer packetBuffer) {
        String readString = packetBuffer.readString(Short.MAX_VALUE);
        InGameOverlays inGameOverlays = GuiFactory.fromString(readString, InGameOverlays.class);
        LocalDataManager.setInGameOverlays(inGameOverlays);
    }
}
