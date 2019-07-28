package com.github.timmyovo.pixeluitweaks.client.gui.overlay;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientRenderMethod;
import com.github.timmyovo.pixeluitweaks.client.hook.SidebarManager;
import com.github.timmyovo.pixeluitweaks.client.packet.manager.LocalDataManager;
import com.github.timmyovo.pixeluitweaks.client.utils.TextureUtils;
import com.github.timmyovo.pixeluitweaks.common.gui.InGameOverlays;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiInGameOverlay extends Gui {
    private int width;
    private int height;

    public void onResolutionChanged(ScaledResolution scaledResolution) {
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
        InGameOverlays inGameOverlays = LocalDataManager.getInGameOverlays();
        if (inGameOverlays != null) {
            ClientRenderMethod clientRenderMethod = ClientRenderMethod.fromRenderMethod(inGameOverlays.getRenderMethod());
            TextureBinder textureBinder = inGameOverlays.getTextureBinder();
            TextureUtils.tryBindTexture(textureBinder);
            for (ClientRenderMethod.ClientRenderEntry renderEntry : clientRenderMethod.getEntryList()) {
                GlStateManager.color(1, 1, 1, 1);
                GlStateManager.enableBlend();
                drawModalRectWithCustomSizedTexture(renderEntry.getXOffset(), renderEntry.getYOffset(), renderEntry.getTextureX(), renderEntry.getTextureY(), renderEntry.getScaledWidth(), renderEntry.getScaledHeight(), renderEntry.getTextureWidth(), renderEntry.getTextureHeight());
                GlStateManager.disableBlend();
            }

        }
    }
}
