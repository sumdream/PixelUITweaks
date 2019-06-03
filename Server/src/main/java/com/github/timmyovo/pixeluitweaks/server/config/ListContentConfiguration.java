package com.github.timmyovo.pixeluitweaks.server.config;

import com.github.timmyovo.pixeluitweaks.common.gui.sidebar.Sidebar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ListContentConfiguration {
    private List<Sidebar> sidebarList;
}
