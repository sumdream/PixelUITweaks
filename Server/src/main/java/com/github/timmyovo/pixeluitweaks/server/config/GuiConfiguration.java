package com.github.timmyovo.pixeluitweaks.server.config;

import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GuiConfiguration {
    private List<GuiEntry> guiEntryList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class GuiEntry {
        private String name;
        private boolean isSlotUI;
        private ComponentContainer guiLayoutBase;
    }
}
