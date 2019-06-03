package com.github.timmyovo.pixeluitweaks.server.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommandEntry {
    private List<SingleCommandEntry> commandEntries;

    public void execute(Player player) {
        commandEntries.forEach(singleCommandEntry -> singleCommandEntry.execute(player));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class SingleCommandEntry {
        private String command;
        private boolean enablePlaceholder;
        private boolean ignorePermission;

        public void execute(Player player) {
            if (isIgnorePermission()) {
                boolean op = player.isOp();
                player.setOp(true);
                Bukkit.dispatchCommand(player, command);
                player.setOp(op);
                return;
            }
            Bukkit.dispatchCommand(player, command);
        }
    }
}
