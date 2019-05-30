package com.github.timmyovo.pixeluitweaks.server.listener;

import com.github.timmyovo.pixeluitweaks.common.event.GuiEventType;
import com.github.timmyovo.pixeluitweaks.common.event.type.*;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import com.github.timmyovo.pixeluitweaks.server.events.*;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class EventListener implements PluginMessageListener {
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(Unpooled.wrappedBuffer(message));
        String eventTypeString = packetDataSerializer.e(32);
        String jsonString = packetDataSerializer.e(Short.MAX_VALUE);
        try {
            GuiEventType eventType = GuiEventType.valueOf(eventTypeString);
            switch (eventType) {
                case MOUSE_EVENT:
                    MouseInputModel mouseInputModel = GuiFactory.fromString(jsonString, MouseInputModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiMouseEvent(player, mouseInputModel));
                    break;
                case KEYBOARD_EVENT:
                    KeyboardInputModel keyboardInputModel = GuiFactory.fromString(jsonString, KeyboardInputModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiKeyboardEvent(player, keyboardInputModel));
                    break;
                case OPEN_SCREEN:
                    OpenScreenModel openScreenModel = GuiFactory.fromString(jsonString, OpenScreenModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiOpenEvent(player, openScreenModel));
                    break;
                case CHECKBOX_CLICK:
                    CheckBoxClickModel checkBoxClickModel = GuiFactory.fromString(jsonString, CheckBoxClickModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiCheckboxClickEvent(player, checkBoxClickModel));
                    break;
                case BUTTON_CLICK:
                    ButtonClickModel buttonClickModel = GuiFactory.fromString(jsonString, ButtonClickModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiButtonClickEvent(player, buttonClickModel));
                    break;
                case CLOSE_SCREEN:
                    CloseScreenModel closeScreenModel = GuiFactory.fromString(jsonString, CloseScreenModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiCloseEvent(player, closeScreenModel));
                    break;
                case OPEN_CONTAINER:
                    ContainerOpenModel containerOpenModel = GuiFactory.fromString(jsonString, ContainerOpenModel.class);
                    Bukkit.getPluginManager().callEvent(new ContainerOpenEvent(player, containerOpenModel));
                    break;
                case CLOSE_CONTAINER:
                    ContainerCloseModel containerCloseModel = GuiFactory.fromString(jsonString, ContainerCloseModel.class);
                    Bukkit.getPluginManager().callEvent(new ContainerCloseEvent(player, containerCloseModel));
                    break;
                case TEXTFIELD_INPUT:
                    TextfieldInputModel textfieldInputModel = GuiFactory.fromString(jsonString, TextfieldInputModel.class);
                    Bukkit.getPluginManager().callEvent(new TextfieldInputEvent(player, textfieldInputModel));
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("无法辨别事件类型...");
        }
    }
}
