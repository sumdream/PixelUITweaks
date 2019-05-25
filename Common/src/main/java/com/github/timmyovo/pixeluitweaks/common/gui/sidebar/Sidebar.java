package com.github.timmyovo.pixeluitweaks.common.gui.sidebar;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Sidebar {
    private String name;
    private SidebarType sidebarType;
    private List<String> strings;
    private String xOffset;
    private String yOffset;
}
