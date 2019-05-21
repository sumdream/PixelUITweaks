package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentLabel;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

@Getter
@Setter
public class GuiLabelImpl extends Gui implements ClientComponent {
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
        this.minecraft = Minecraft.getMinecraft();
        this.x = componentLabel.getXPos();
        this.y = componentLabel.getYPos();
        this.width = componentLabel.getWidth();
        this.height = componentLabel.getHeight();
        this.centered = componentLabel.isCentered();
        this.visible = componentLabel.isVisible();
        this.textColor = componentLabel.getTextColor();
        this.border = componentLabel.getBorder();
        this.fontRenderer = minecraft.fontRenderer;
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

    protected void drawLabelBackground(Minecraft mcIn, int mouseX, int mouseY) {

    }
}
