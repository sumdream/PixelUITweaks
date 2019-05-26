package com.github.timmyovo.pixeluitweaks.client.hook;

import com.github.timmyovo.pixeluitweaks.common.gui.sidebar.Sidebar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.ArrayList;
import java.util.List;

public class SidebarManager {
    public static List<ClientSidebar> clientSidebarList = new ArrayList<>();
    private static List<Sidebar> serverSidebarModelList = new ArrayList<>();

    static {

    }

    public static void clear() {
        serverSidebarModelList.clear();
        clientSidebarList.clear();
    }

    public static void updateClientModels() {
        clientSidebarList.clear();
        for (Sidebar sidebar : serverSidebarModelList) {
            clientSidebarList.add(ClientSidebar.from(sidebar));
        }
    }

    public static boolean addSidebar(Sidebar sidebar) {
        if (hasSidebar(sidebar)) {
            removeSidebar(sidebar);
        }
        boolean add = serverSidebarModelList.add(sidebar);
        updateClientModels();
        return add;
    }

    public static boolean removeSidebar(Sidebar sidebar) {
        boolean b = serverSidebarModelList.removeIf(clientSidebar -> clientSidebar.getName().equalsIgnoreCase(sidebar.getName()));
        updateClientModels();
        return b;
    }

    public static boolean hasSidebar(Sidebar sidebar) {
        return serverSidebarModelList.stream().anyMatch(sidebar1 -> sidebar1.getName().equalsIgnoreCase(sidebar.getName()));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClientSidebar {
        private String name;
        private List<String> strings;
        private int xOffset;
        private int yOffset;

        public static ClientSidebar from(Sidebar sidebar) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            int x = (int) new ExpressionBuilder(sidebar.getXOffset())
                    .variables("w", "h")
                    .build()
                    .setVariable("w", scaledResolution.getScaledWidth())
                    .setVariable("h", scaledResolution.getScaledHeight())
                    .evaluate();
            int y = (int) new ExpressionBuilder(sidebar.getYOffset())
                    .variables("w", "h")
                    .build()
                    .setVariable("w", scaledResolution.getScaledWidth())
                    .setVariable("h", scaledResolution.getScaledHeight())
                    .evaluate();
            return ClientSidebar.builder()
                    .name(sidebar.getName())
                    .xOffset(x)
                    .yOffset(y)
                    .strings(sidebar.getStrings())
                    .build();
        }
    }
}
