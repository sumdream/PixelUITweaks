package com.github.timmyovo.pixeluitweaks.client.gui;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;

public interface ClientComponent<T extends AbstractComponent> {
    default void render(int mouseX, int mouseY, float ticks) {

    }

    default void playPressSound(SoundHandler soundHandlerIn) {

    }

    default boolean isMouseOver() {
        return false;
    }

    default boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return false;
    }

    default void mouseReleased(int mouseX, int mouseY) {
    }

    T getComponentModel();

    void updateComponent(T componentModel);
}
