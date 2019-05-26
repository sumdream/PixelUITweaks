package com.github.timmyovo.pixeluitweaks.client.gui.overlay;

import com.github.timmyovo.pixeluitweaks.client.hook.SidebarManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiInGameOverlay extends Gui {
    private int width;
    private int height;

    public void onResolutionChanged(ScaledResolution scaledResolution) {
        System.out.println(111);
        this.width = scaledResolution.getScaledWidth();
        this.height = scaledResolution.getScaledHeight();
        SidebarManager.updateClientModels();
    }


    @SubscribeEvent
    public void onPlayerRenderScoreboard(RenderGameOverlayEvent.Post renderGameOverlayEvent) {
        if (renderGameOverlayEvent.getType() != RenderGameOverlayEvent.ElementType.ALL) {
            return;
        }
        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        ScaledResolution resolution = renderGameOverlayEvent.getResolution();
        if (width != resolution.getScaledWidth() || height != resolution.getScaledHeight()) {
            onResolutionChanged(resolution);
        }
        for (SidebarManager.ClientSidebar sidebar : SidebarManager.clientSidebarList) {

            int i1 = SidebarManager.clientSidebarList.size() * fontRenderer.FONT_HEIGHT;
            int j1 = resolution.getScaledHeight() / 2 + i1 / 3;

            int j = 0;


            for (String score1 : sidebar.getStrings()) {
                int i = fontRenderer.getStringWidth(score1);
                int l1 = resolution.getScaledWidth() - i - 3 + sidebar.getXOffset();
                ++j;
                int k = j1 - j * fontRenderer.FONT_HEIGHT + sidebar.getYOffset();
                int l = resolution.getScaledWidth() - 3 + 2 + sidebar.getXOffset();
                drawRect(l1 - 2, k, l, k + fontRenderer.FONT_HEIGHT, 1342177280);
                fontRenderer.drawString(score1, l1, k, 553648127);
            }
        }

    }
}
