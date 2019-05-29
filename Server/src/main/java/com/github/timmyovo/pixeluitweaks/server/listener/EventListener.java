package com.github.timmyovo.pixeluitweaks.server.listener;

import com.github.timmyovo.pixeluitweaks.common.event.GuiEventType;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class EventListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(Unpooled.wrappedBuffer(message));
        String eventTypeString = packetDataSerializer.e(32);
        try {
            GuiEventType eventType = GuiEventType.valueOf(eventTypeString);
        } catch (IllegalArgumentException e) {
            System.out.println("无法辨别事件类型...");
        }
    }
}
