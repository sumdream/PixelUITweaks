package com.github.timmyovo.pixeluitweaks.server.packet;

import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentSlot;
import com.github.timmyovo.pixeluitweaks.common.gui.sidebar.Sidebar;
import com.github.timmyovo.pixeluitweaks.server.inventory.ItemHandler;
import com.github.timmyovo.pixeluitweaks.server.inventory.ServerDummyContainer;
import com.github.timmyovo.pixeluitweaks.server.inventory.network.OpenGui;
import com.github.timmyovo.pixeluitweaks.server.packet.out_.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.Container;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.awt.image.BufferedImage;
import java.util.stream.Collectors;

public class PacketManager implements IComp<PacketManager> {
    public static void openGui(EntityPlayer entityPlayer, Container container, int hashcode) {
        int windowId = entityPlayer.nextContainerCounter();
        entityPlayer.closeInventory();

        OpenGui examplemod = new OpenGui(windowId, "pixeluitweaks", hashcode, 0, 0, 0);
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeByte(1);
        examplemod.toBytes(buffer);
        PacketPlayOutCustomPayload wslnm = new PacketPlayOutCustomPayload("FML", new PacketDataSerializer(buffer));
        entityPlayer.playerConnection.sendPacket(wslnm);
        entityPlayer.activeContainer = container;
        entityPlayer.activeContainer.windowId = windowId;
        entityPlayer.activeContainer.addSlotListener(entityPlayer);
    }

    public void openScreen(Player player, ComponentContainer componentContainer) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketInOpenScreen packetInOpenScreen = new PacketInOpenScreen(componentContainer);
        packetInOpenScreen.sendPacket(handle);
    }

    public void openContainerScreen(Player player, ComponentContainer componentContainer) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketInOpenContainerScreen packetInOpenContainerScreen = new PacketInOpenContainerScreen(0, componentContainer);
        packetInOpenContainerScreen.sendPacket(handle);
        openGui(handle, new ServerDummyContainer(handle, new ItemHandler(4), componentContainer.getComponentList()
                .stream()
                .filter(abstractComponent -> abstractComponent instanceof ComponentSlot)
                .map(abstractComponent -> ((ComponentSlot) abstractComponent))
                .collect(Collectors.toList())), componentContainer.hashCode());
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

    public void sendListContent(Player player, Sidebar sidebar) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketOutListContent packetOutListContent = new PacketOutListContent(sidebar);
        packetOutListContent.sendPacket(handle);
    }

    @Override
    public PacketManager init() {
        return this;
    }
}
