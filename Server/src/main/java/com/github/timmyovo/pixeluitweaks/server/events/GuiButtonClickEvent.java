package com.github.timmyovo.pixeluitweaks.server.events;

import com.github.timmyovo.pixeluitweaks.common.event.type.ButtonClickModel;
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
public class GuiButtonClickEvent extends Event {
    private static final HandlerList handlers = new HandlerList();
    private Player player;
    private ButtonClickModel guiButton;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
