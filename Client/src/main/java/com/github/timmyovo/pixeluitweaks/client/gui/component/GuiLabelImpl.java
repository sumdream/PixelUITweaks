package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentLabel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.objecthunter.exp4j.ExpressionBuilder;

@Getter
@Setter
public class GuiLabelImpl extends Gui implements ClientComponent<ComponentLabel> {
    public int x;
    public int y;
    public boolean visible = true;
    protected int width = 200;
    protected int height = 20;
    protected int border;
    private ComponentLabel componentLabel;
    private Minecraft minecraft;
    private boolean centered;
    private int textColor;
    private FontRenderer fontRenderer;

    public GuiLabelImpl(ComponentLabel componentLabel) {
        this.componentLabel = componentLabel;
        updateComponent(componentLabel);
    }

    @Override
    public void render(int mouseX, int mouseY, float ticks) {
        if (visible) {
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            this.drawLabelBackground(minecraft, mouseX, mouseY);
            int i = this.y + this.height / 2 + this.border / 2;
            int j = i - this.componentLabel.getLabels().size() * 10 / 2;

            for (int k = 0; k < this.componentLabel.getLabels().size(); ++k) {
                if (this.centered) {
                    this.drawCenteredString(this.fontRenderer, this.componentLabel.getLabels().get(k), this.x + this.width / 2, j + k * 10, this.textColor);
                } else {
                    this.drawString(this.fontRenderer, this.componentLabel.getLabels().get(k), this.x, j + k * 10, this.textColor);
                }
            }
        }
    }

    @Override
    public void updateComponent(ComponentLabel componentModel) {
        this.minecraft = Minecraft.getMinecraft();
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
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
        this.centered = componentModel.isCentered();
        this.visible = componentModel.isVisible();
        this.textColor = componentModel.getTextColor();
        this.border = componentModel.getBorder();
        this.fontRenderer = minecraft.fontRenderer;
    }

    protected void drawLabelBackground(Minecraft mcIn, int mouseX, int mouseY) {

    }
}
