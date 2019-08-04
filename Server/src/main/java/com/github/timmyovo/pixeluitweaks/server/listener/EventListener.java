package com.github.timmyovo.pixeluitweaks.server.listener;

import com.github.timmyovo.pixeluitweaks.common.event.GuiEventType;
import com.github.timmyovo.pixeluitweaks.common.event.type.*;
import com.github.timmyovo.pixeluitweaks.common.message.GuiFactory;
import com.github.timmyovo.pixeluitweaks.server.PixelUITweaksServer;
import com.github.timmyovo.pixeluitweaks.server.config.CallbackConfiguration;
import com.github.timmyovo.pixeluitweaks.server.config.CommandEntry;
import com.github.timmyovo.pixeluitweaks.server.events.*;
import com.github.timmyovo.pixeluitweaks.server.manager.CallbackManager;
import com.github.timmyovo.pixeluitweaks.server.manager.PlayerStateManager;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EventListener implements PluginMessageListener {
    public static <T> void testFor(Supplier<T> booleanSupplier, Consumer<T> tConsumer) {
        T t = booleanSupplier.get();
        if (t != null) {
            tConsumer.accept(t);
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        PacketDataSerializer packetDataSerializer = new PacketDataSerializer(Unpooled.wrappedBuffer(message));
        String eventTypeString = packetDataSerializer.e(32);
        String jsonString = packetDataSerializer.e(Short.MAX_VALUE);
        PixelUITweaksServer pixelUiTweaksServer = PixelUITweaksServer.getPixelUiTweaksServer();
        CallbackManager callbackManager = PixelUITweaksServer.getModule(CallbackManager.class);
        CallbackConfiguration callbackConfiguration = pixelUiTweaksServer.getCallbackConfiguration();
        Map<UUID, CommandEntry> guiContextCallback = callbackConfiguration.getGuiContextCallback();
        try {
            GuiEventType eventType = GuiEventType.valueOf(eventTypeString);
            switch (eventType) {
                case MOUSE_EVENT:
                    MouseInputModel mouseInputModel = GuiFactory.fromString(jsonString, MouseInputModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiMouseEvent(player, mouseInputModel));
                    callbackManager.notifyEvent(mouseInputModel, player);
                    break;
                case KEYBOARD_EVENT:
                    KeyboardInputModel keyboardInputModel = GuiFactory.fromString(jsonString, KeyboardInputModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiKeyboardEvent(player, keyboardInputModel));
                    callbackManager.notifyEvent(keyboardInputModel, player);
                    break;
                case CHECKBOX_CLICK:
                    CheckBoxClickModel checkBoxClickModel = GuiFactory.fromString(jsonString, CheckBoxClickModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiCheckboxClickEvent(player, checkBoxClickModel));
                    testFor(() -> guiContextCallback.get(checkBoxClickModel.getComponentCheckBox().getComponentId()), commandEntry -> {
                        commandEntry.execute(player);
                    });
                    callbackManager.notifyComponentEvent(checkBoxClickModel);
                    break;
                case BUTTON_CLICK:
                    ButtonClickModel buttonClickModel = GuiFactory.fromString(jsonString, ButtonClickModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiButtonClickEvent(player, buttonClickModel));
                    testFor(() -> guiContextCallback.get(buttonClickModel.getComponentButton().getComponentId()), commandEntry -> {
                        commandEntry.execute(player);
                    });
                    callbackManager.notifyComponentEvent(buttonClickModel);
                    break;
                case OPEN_SCREEN:
                    OpenScreenModel openScreenModel = GuiFactory.fromString(jsonString, OpenScreenModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiOpenEvent(player, openScreenModel));
                    PlayerStateManager.notifyPlayerGuiOpen(player, openScreenModel.getScreenContainers());
                    callbackManager.notifyEvent(openScreenModel, player);
                    break;
                case CLOSE_SCREEN:
                    CloseScreenModel closeScreenModel = GuiFactory.fromString(jsonString, CloseScreenModel.class);
                    Bukkit.getPluginManager().callEvent(new GuiCloseEvent(player, closeScreenModel));
                    PlayerStateManager.notifyPlayerClose(player, closeScreenModel.getScreenContainers());
                    callbackManager.notifyEvent(closeScreenModel, player);
                    break;
                case OPEN_CONTAINER:
                    ContainerOpenModel containerOpenModel = GuiFactory.fromString(jsonString, ContainerOpenModel.class);
                    Bukkit.getPluginManager().callEvent(new ContainerOpenEvent(player, containerOpenModel));
                    PlayerStateManager.notifyPlayerGuiOpen(player, containerOpenModel.getOpenedContainer());
                    callbackManager.notifyEvent(containerOpenModel, player);
                    break;
                case CLOSE_CONTAINER:
                    ContainerCloseModel containerCloseModel = GuiFactory.fromString(jsonString, ContainerCloseModel.class);
                    Bukkit.getPluginManager().callEvent(new ContainerCloseEvent(player, containerCloseModel));
                    PlayerStateManager.notifyPlayerClose(player, containerCloseModel.getClosedContainer());
                    callbackManager.notifyEvent(containerCloseModel, player);
                    break;
                case TEXTFIELD_INPUT:
                    TextfieldInputModel textfieldInputModel = GuiFactory.fromString(jsonString, TextfieldInputModel.class);
                    Bukkit.getPluginManager().callEvent(new TextfieldInputEvent(player, textfieldInputModel));
                    PlayerStateManager.notifyInputTextChanged(player, textfieldInputModel.getComponentTextField(), textfieldInputModel.getInputText());
                    callbackManager.notifyComponentEvent(textfieldInputModel);
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.out.println("无法辨别事件类型...");
        }
    }
}
