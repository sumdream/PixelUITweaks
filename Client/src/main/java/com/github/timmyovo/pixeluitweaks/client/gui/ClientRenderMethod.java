package com.github.timmyovo.pixeluitweaks.client.gui;

import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRenderMethod {
    @SerializedName("entryList")
    private List<ClientRenderEntry> entryList;

    public static ClientRenderMethod fromRenderMethod(RenderMethod renderMethod) {
        return new ClientRenderMethod(renderMethod.getEntryList().stream().map(ClientRenderEntry::fromRenderEntry).collect(Collectors.toList()));
    }

    public void render() {
        getEntryList().forEach(ClientRenderEntry::render);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClientRenderEntry {
        @SerializedName("xOffset")
        private int xOffset;
        @SerializedName("yOffset")
        private int yOffset;
        @SerializedName("textureX")
        private int textureX;
        @SerializedName("textureY")
        private int textureY;
        @SerializedName("textureWidth")
        private int textureWidth;
        @SerializedName("textureHeight")
        private int textureHeight;
        @SerializedName("scaledWidth")
        private int scaledWidth;
        @SerializedName("scaledHeight")
        private int scaledHeight;

        public static ClientRenderEntry fromRenderEntry(RenderMethod.RenderEntry renderEntry) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            int scaledWidth = scaledResolution.getScaledWidth();
            int scaledHeight = scaledResolution.getScaledHeight();

            int x = (int) new ExpressionBuilder(renderEntry.getXOffset())
                    .variables("w", "h")
                    .build()
                    .setVariable("w", scaledWidth)
                    .setVariable("h", scaledHeight)
                    .evaluate();
            int y = (int) new ExpressionBuilder(renderEntry.getYOffset())
                    .variables("w", "h")
                    .build()
                    .setVariable("w", scaledWidth)
                    .setVariable("h", scaledHeight)
                    .evaluate();
            int tileWidth = (int) new ExpressionBuilder(renderEntry.getTextureWidth())
                    .variables("w", "h")
                    .build()
                    .setVariable("w", scaledWidth)
                    .setVariable("h", scaledHeight)
                    .evaluate();
            int tileHeight = (int) new ExpressionBuilder(renderEntry.getTextureHeight())
                    .variables("w", "h")
                    .build()
                    .setVariable("w", scaledWidth)
                    .setVariable("h", scaledHeight)
                    .evaluate();
            int scaledHeightL = (int) new ExpressionBuilder(renderEntry.getScaledHeight())
                    .variables("w", "h")
                    .build()
                    .setVariable("w", scaledWidth)
                    .setVariable("h", scaledHeight)
                    .evaluate();
            int scaledWidthL = (int) new ExpressionBuilder(renderEntry.getScaledWidth())
                    .variables("w", "h")
                    .build()
                    .setVariable("w", scaledWidth)
                    .setVariable("h", scaledHeight)
                    .evaluate();
            return ClientRenderEntry.builder()
                    .xOffset(x)
                    .yOffset(y)
                    .textureX(renderEntry.getTextureX())
                    .textureY(renderEntry.getTextureY())
                    .textureHeight(tileHeight)
                    .textureWidth(tileWidth)
                    .scaledHeight(scaledHeightL)
                    .scaledWidth(scaledWidthL)
                    .build();
        }

        public void render() {
            ClientRenderEntry renderEntry = this;
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableBlend();
            Gui.drawModalRectWithCustomSizedTexture(renderEntry.getXOffset(), renderEntry.getYOffset(), renderEntry.getTextureX(), renderEntry.getTextureY(), renderEntry.getTextureWidth(), renderEntry.getTextureHeight(), renderEntry.getScaledWidth(), renderEntry.getScaledHeight());
            GlStateManager.disableBlend();
        }
    }
}