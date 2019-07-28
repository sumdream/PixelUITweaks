package com.github.timmyovo.pixeluitweaks.server.packet;

import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.InGameOverlays;
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
        PacketOutOpenScreen packetOutOpenScreen = new PacketOutOpenScreen(componentContainer);
        packetOutOpenScreen.sendPacket(handle);
    }

    public void openContainerScreen(Player player, ComponentContainer componentContainer) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketOutOpenContainerScreen packetOutOpenContainerScreen = new PacketOutOpenContainerScreen(0, componentContainer);
        packetOutOpenContainerScreen.sendPacket(handle);
        openGui(handle, new ServerDummyContainer(handle, new ItemHandler(4), componentContainer.getComponentList()
                .stream()
                .filter(abstractComponent -> abstractComponent instanceof ComponentSlot)
                .map(abstractComponent -> ((ComponentSlot) abstractComponent))
                .collect(Collectors.toList())), componentContainer.hashCode());
    }

    public void addContainer(Player player, ComponentContainer componentContainer) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketOutAddContainer packetOutAddContainer = new PacketOutAddContainer(componentContainer);
        packetOutAddContainer.sendPacket(handle);
    }

    public void removeContainer(Player player, ComponentContainer componentContainer) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketOutRemoveContainer packetOutRemoveContainer = new PacketOutRemoveContainer(componentContainer);
        packetOutRemoveContainer.sendPacket(handle);
    }

    public void closePlayerGui(Player player) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketOutCloseScreen packetOutCloseScreen = new PacketOutCloseScreen();
        packetOutCloseScreen.sendPacket(handle);
    }

    public void sendTextureToPlayer(Player player, String name, BufferedImage bufferedImage) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketOutRecvTexture packetOutRecvTexture = new PacketOutRecvTexture(name, bufferedImage);
        packetOutRecvTexture.sendPacket(handle);
    }

    public void sendListContent(Player player, Sidebar sidebar) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketOutListContent packetOutListContent = new PacketOutListContent(sidebar);
        packetOutListContent.sendPacket(handle);
    }

    public void setPlayerOverlay(Player player, InGameOverlays inGameOverlays) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        PacketOutSetOverlays packetOutSetOverlays = new PacketOutSetOverlays(inGameOverlays);
        packetOutSetOverlays.sendPacket(handle);
    }

    @Override
    public PacketManager init() {
        return this;
    }
}
