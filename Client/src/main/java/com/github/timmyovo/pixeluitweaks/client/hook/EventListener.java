package com.github.timmyovo.pixeluitweaks.client.hook;

import com.github.timmyovo.pixeluitweaks.client.packet.out_.PacketOutEvent;
import com.github.timmyovo.pixeluitweaks.common.event.GuiEventType;
import com.github.timmyovo.pixeluitweaks.common.event.type.KeyboardInputModel;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

import java.util.Collections;

public class EventListener {
    @SubscribeEvent
    public void onClientDisconnected(FMLNetworkEvent.ClientDisconnectionFromServerEvent clientDisconnectionFromServerEvent) {
        SidebarManager.clear();
    }

    @SubscribeEvent
    public void onPlayerKeyPress(InputEvent.KeyInputEvent keyInputEvent) {
        PacketOutEvent.notifyEvent(GuiEventType.KEYBOARD_EVENT, KeyboardInputModel.builder()
                .inputEntry(KeyboardInputModel.InputEntry.builder()
                        .componentContainer(Collections.emptyList())
                        .keycode(Keyboard.getEventKey())
                        .typedChar(Keyboard.getEventCharacter())
                        .build())
                .build());
    }
}
