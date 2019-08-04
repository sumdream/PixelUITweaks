package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentEntityRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.objecthunter.exp4j.ExpressionBuilder;

public class GuiEntityRendererImpl extends Gui implements ClientComponent<ComponentEntityRenderer> {
    public int width;
    public int height;
    public int x;
    public int y;
    public boolean visible;
    private ComponentEntityRenderer componentEntityRenderer;

    public GuiEntityRendererImpl(ComponentEntityRenderer componentEntityRenderer) {
        this.componentEntityRenderer = componentEntityRenderer;
        updateComponent(getComponentModel());
    }

    @Override
    public void render(int mouseX, int mouseY, float ticks) {
        if (visible) {
            EntityPlayerSP player = Minecraft.getMinecraft().player;
            if (player == null) {
                return;
            }
            GuiInventory.drawEntityOnScreen(x, y, 30, (float) (51) - mouseX, (float) (75 - 50) - mouseY, player);
        }
    }

    @Override
    public ComponentEntityRenderer getComponentModel() {
        return componentEntityRenderer;
    }

    @Override
    public void updateComponent(ComponentEntityRenderer componentModel) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        int scaledHeight = scaledResolution.getScaledHeight();
        int scaledWidth = scaledResolution.getScaledWidth();

        this.width = (int) new ExpressionBuilder(componentModel.getWidth())
                .variables("w", "h")
                .build()
                .setVariable("w", scaledWidth)
                .setVariable("h", scaledHeight)
                .evaluate();
        this.height = (int) new ExpressionBuilder(componentModel.getHeight())
                .variables("w", "h")
                .build()
                .setVariable("w", scaledWidth)
                .setVariable("h", scaledHeight)
                .evaluate();
        this.x = (int) new ExpressionBuilder(componentModel.getXPos())
                .variables("w", "h")
                .build()
                .setVariable("w", scaledWidth)
                .setVariable("h", scaledHeight)
                .evaluate();
        this.y = (int) new ExpressionBuilder(componentModel.getYPos())
                .variables("w", "h")
                .build()
                .setVariable("w", scaledWidth)
                .setVariable("h", scaledHeight)
                .evaluate();
        this.visible = componentModel.isVisible();
    }
}
