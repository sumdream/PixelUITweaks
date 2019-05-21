package com.github.timmyovo.pixeluitweaks.client.hook;

import com.github.timmyovo.pixeluitweaks.client.PixelUITweaks;
import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

public class ServerPacketListener implements IComp<ServerPacketListener> {
    public static final String GUI_CHANNEL_NAME = "PUU";
    public static final String GUI_INVENTORY_CHANNEL_NAME = "PUU|INV";
    public static final String GUI_CHANNEL_EVENT_NAME = "PUU|EVENT";
    public static final String DATA_CHANNEL_NAME = "PUU|PIC";
    private FMLEventChannel guiEventChannel;
    private FMLEventChannel pictureEventChannel;
    private FMLEventChannel inventoryEventChannel;

    @SubscribeEvent
    public void onClientReceivePacket(FMLNetworkEvent.ClientCustomPacketEvent clientCustomPacketEvent) {
        FMLProxyPacket packet = clientCustomPacketEvent.getPacket();
        String channel = packet.channel();
        ClientHook clientHook = PixelUITweaks.INSTANCE.getClientHook();
        if (channel.equals(GUI_CHANNEL_NAME)) {
            clientHook.notifyGuiReceive(packet);
            return;
        }
        if (channel.equalsIgnoreCase(GUI_INVENTORY_CHANNEL_NAME)) {
            clientHook.notifyInventoryReceive(packet);
        }
        if (channel.equals(DATA_CHANNEL_NAME)) {
            clientHook.notifyPictureReceive(packet);
        }
    }

    @Override
    public ServerPacketListener init() {
        this.guiEventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(GUI_CHANNEL_NAME);
        this.guiEventChannel.register(this);
        this.pictureEventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(DATA_CHANNEL_NAME);
        this.pictureEventChannel.register(this);
        this.inventoryEventChannel = NetworkRegistry.INSTANCE.newEventDrivenChannel(GUI_INVENTORY_CHANNEL_NAME);
        this.inventoryEventChannel.register(this);
        return this;
    }
}
