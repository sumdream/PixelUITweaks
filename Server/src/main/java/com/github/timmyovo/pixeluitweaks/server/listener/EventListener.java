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
            switch (eventType) {
                case MOUSE_EVENT:
                    break;
                case KEYBOARD_EVENT:
                    break;
                case OPEN_SCREEN:
                    break;
                case CHECKBOX_CLICK:
                    break;
                case BUTTON_CLICK:
                    break;
                case CLOSE_SCREEN:
                    break;
                case OPEN_CONTAINER:
                    break;
                case CLOSE_CONTAINER:
                    break;
                case TEXTFIELD_INPUT:
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("无法辨别事件类型...");
        }
    }
}
