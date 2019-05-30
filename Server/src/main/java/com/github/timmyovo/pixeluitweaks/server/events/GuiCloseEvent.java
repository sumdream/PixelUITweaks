package com.github.timmyovo.pixeluitweaks.server.events;

import com.github.timmyovo.pixeluitweaks.common.event.type.CloseScreenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@AllArgsConstructor
public class GuiCloseEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private CloseScreenModel guiCloseEvent;

    public static HandlerList getHandlerList() {
        return handlers;
    }


    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
