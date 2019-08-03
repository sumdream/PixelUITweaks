package com.github.timmyovo.pixeluitweaks.client.gui;

import com.github.timmyovo.pixeluitweaks.common.render.ItemRenderer;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.objecthunter.exp4j.ExpressionBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientItemRenderer {
    @SerializedName(value = "itemName")
    private ItemStack itemName;
    @SerializedName(value = "itemRendererMeta")
    private ClientItemRenderer.ClientItemRendererMeta itemRendererMeta;

    public static ClientItemRenderer fromItemRenderer(ItemRenderer itemRenderer) {
        Item value = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemRenderer.getItemName()));
        if (value == null) {
            value = Items.APPLE;
        }
        ItemStack itemStackToRender = new ItemStack(value);
        return ClientItemRenderer.builder()
                .itemName(itemStackToRender)
                .itemRendererMeta(ClientItemRendererMeta.fromRenderEntry(itemRenderer.getItemRendererMeta()))
                .build();
    }

    public void render() {
        RenderHelper.disableStandardItemLighting();
        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(itemName, itemRendererMeta.x, itemRendererMeta.y);
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ClientItemRendererMeta {
        private int x;
        private int y;

        public static ClientItemRendererMeta fromRenderEntry(ItemRenderer.ItemRendererMeta renderEntry) {
            ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
            int scaledWidth = scaledResolution.getScaledWidth();
            int scaledHeight = scaledResolution.getScaledHeight();

            int x = (int) new ExpressionBuilder(renderEntry.getX())
                    .variables("w", "h")
                    .build()
                    .setVariable("w", scaledWidth)
                    .setVariable("h", scaledHeight)
                    .evaluate();
            int y = (int) new ExpressionBuilder(renderEntry.getY())
                    .variables("w", "h")
                    .build()
                    .setVariable("w", scaledWidth)
                    .setVariable("h", scaledHeight)
                    .evaluate();
            return ClientItemRendererMeta.builder()
                    .x(x)
                    .y(y)
                    .build();
        }
    }
}
