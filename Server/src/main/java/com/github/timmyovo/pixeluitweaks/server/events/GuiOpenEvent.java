package com.github.timmyovo.pixeluitweaks.server.events;

import com.github.timmyovo.pixeluitweaks.common.event.type.OpenScreenModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Data
@AllArgsConstructor
public class GuiOpenEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private OpenScreenModel guiOpenModel;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
