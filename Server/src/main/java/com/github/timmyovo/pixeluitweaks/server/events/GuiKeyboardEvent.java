package com.github.timmyovo.pixeluitweaks.server.events;

import com.github.timmyovo.pixeluitweaks.common.event.type.KeyboardInputModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuiKeyboardEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private KeyboardInputModel keyboardEventModel;

    public static HandlerList getHandlerList() {
        return handlers;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
