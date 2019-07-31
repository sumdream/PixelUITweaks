package com.github.timmyovo.pixeluitweaks.client.packet.manager;

import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.InGameOverlays;
import com.google.common.collect.Lists;

import java.util.List;

public class LocalDataManager {
    private static List<ComponentContainer> commonComponentContainers = Lists.newArrayList();
    private static List<ComponentContainer> slotComponentContainers = Lists.newArrayList();
    private static InGameOverlays inGameOverlays = null;

    public static InGameOverlays getInGameOverlays() {
        return inGameOverlays;
    }

    public static void setInGameOverlays(InGameOverlays inGameOverlays) {
        LocalDataManager.inGameOverlays = inGameOverlays;
    }

    public static List<ComponentContainer> getCommonComponentContainers() {
        return commonComponentContainers;
    }

    public static void setCommonComponentContainers(List<ComponentContainer> commonComponentContainers) {
        LocalDataManager.commonComponentContainers = commonComponentContainers;
    }

    public static List<ComponentContainer> getSlotComponentContainers() {
        return slotComponentContainers;
    }

    public static void setSlotComponentContainers(List<ComponentContainer> slotComponentContainers) {
        LocalDataManager.slotComponentContainers = slotComponentContainers;
    }
}
