package com.github.timmyovo.pixeluitweaks.client.packet;

import com.github.timmyovo.pixeluitweaks.client.gui.CommonUIScreen;
import com.github.timmyovo.pixeluitweaks.client.gui.SlotUIScreen;
import com.github.timmyovo.pixeluitweaks.client.packet.in.*;
import com.github.timmyovo.pixeluitweaks.client.packet.manager.LocalDataManager;
import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentSlot;
import com.github.timmyovo.pixeluitweaks.common.packet.PacketInTypes;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class PacketManager implements IComp<PacketManager>, IGuiHandler {
    private static final Map<String, IPacketIn> PACKET_INS = new HashMap<>();

    public static void sendPacket(String channel, IPacketOut iPacketOut) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) {
            return;
        }
        NetHandlerPlayClient connection = player.connection;
        if (connection == null) {
            return;
        }
        NetworkManager networkManager = connection.getNetworkManager();
        PacketBuffer packetBuffer = new PacketBuffer(Unpooled.buffer());
        iPacketOut.writePacket(packetBuffer);
        CPacketCustomPayload cPacketCustomPayload = new CPacketCustomPayload(channel, packetBuffer);
        networkManager.sendPacket(cPacketCustomPayload);
    }

    public static void registerPacketIn(String channel, IPacketIn iPacketIn) {
        PACKET_INS.put(channel, iPacketIn);
    }

    @SubscribeEvent
    public void onClientReceivePacket(FMLNetworkEvent.ClientCustomPacketEvent clientCustomPacketEvent) {
        FMLProxyPacket fmlProxyPacket = clientCustomPacketEvent.getPacket();
        String channel = fmlProxyPacket.channel();
        IPacketIn iPacketIn = PACKET_INS.get(channel);
        if (iPacketIn != null) {
            iPacketIn.readPacket(((CPacketCustomPayload) fmlProxyPacket.toC17Packet()).getBufferData());
        }
    }


    @Override
    public PacketManager init() {
        registerPacketIn(PacketInTypes.AddContainer.name(), new PacketInAddContainer());
        registerPacketIn(PacketInTypes.CloseScreen.name(), new PacketInCloseScreen());
        registerPacketIn(PacketInTypes.OpenContainerScreen.name(), new PacketInOpenContainerScreen());
        registerPacketIn(PacketInTypes.OpenScreen.name(), new PacketInOpenScreen());
        registerPacketIn(PacketInTypes.RecvTexture.name(), new PacketInRecvTexture());
        registerPacketIn(PacketInTypes.RemoveContainer.name(), new PacketInRemoveContainer());
        registerPacketIn(PacketInTypes.UpdateOverlayContent.name(), new PacketUpdateOverlayListContent());
        return this;
    }

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        Optional<ComponentContainer> optionalComponentContainer = LocalDataManager.getCommonComponentContainers()
                .stream()
                .collect(Collectors.toCollection(LocalDataManager::getSlotComponentContainers))
                .stream()
                .filter(componentContainer -> componentContainer.hashCode() == ID)
                .findFirst();
        if (optionalComponentContainer
                .isPresent()) {
            ComponentContainer componentContainer = optionalComponentContainer.get();
            if (componentContainer.getComponentList()
                    .stream()
                    .anyMatch(abstractComponent -> abstractComponent instanceof ComponentSlot)) {
                return new SlotUIScreen(player, componentContainer);
            }
            return new CommonUIScreen(player, componentContainer);
        }

        return null;
    }
}
