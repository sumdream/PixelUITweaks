package com.github.timmyovo.pixeluitweaks.server.packet;

import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.server.packet.out_.*;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;

public class PacketManager implements IComp<PacketManager> {
    public void openScreen(Player player, ComponentContainer componentContainer) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketInOpenScreen packetInOpenScreen = new PacketInOpenScreen(componentContainer);
        packetInOpenScreen.sendPacket(handle);
    }

    public void openContainerScreen(Player player, ComponentContainer componentContainer) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketInOpenContainerScreen packetInOpenContainerScreen = new PacketInOpenContainerScreen(handle.nextContainerCounter(), componentContainer);
        packetInOpenContainerScreen.sendPacket(handle);
    }

    public void addContainer(Player player, ComponentContainer componentContainer) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketInAddContainer packetInAddContainer = new PacketInAddContainer(componentContainer);
        packetInAddContainer.sendPacket(handle);
    }

    public void removeContainer(Player player, ComponentContainer componentContainer) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketInRemoveContainer packetInRemoveContainer = new PacketInRemoveContainer(componentContainer);
        packetInRemoveContainer.sendPacket(handle);
    }

    public void closePlayerGui(Player player) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketInCloseScreen packetInCloseScreen = new PacketInCloseScreen();
        packetInCloseScreen.sendPacket(handle);
    }

    public void sendTextureToPlayer(Player player, String name, BufferedImage bufferedImage) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketInRecvTexture packetInRecvTexture = new PacketInRecvTexture(name, bufferedImage);
        packetInRecvTexture.sendPacket(handle);
    }

    @Override
    public PacketManager init() {
        return this;
    }
}
