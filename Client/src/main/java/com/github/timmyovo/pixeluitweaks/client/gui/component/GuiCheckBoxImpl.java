package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentCheckBox;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.config.GuiUtils;

public class GuiCheckBoxImpl extends GuiButtonImpl implements ClientComponent {
    protected int boxWidth;
    private ComponentCheckBox componentCheckBox;
    private boolean isChecked;

    public GuiCheckBoxImpl(ComponentCheckBox componentCheckBox) {
        this.componentCheckBox = componentCheckBox;
        this.mc = Minecraft.getMinecraft();
        this.width = componentCheckBox.getWidth();
        if (width == -1) {
            this.width = this.boxWidth + 2 + Minecraft.getMinecraft().fontRenderer.getStringWidth(displayString);
        }
        this.height = componentCheckBox.getHeight();
        this.x = componentCheckBox.getXPos();
        this.y = componentCheckBox.getYPos();
        this.displayString = componentCheckBox.getDisplayString();
        this.boxWidth = componentCheckBox.getBoxWidth();
        this.visible = componentCheckBox.isVisible();
    }

    @Override
    public void render(int mouseX, int mouseY, float ticks) {
        if (visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.boxWidth && mouseY < this.y + this.height;
            GuiUtils.drawContinuousTexturedBox(BUTTON_TEXTURES, this.x, this.y, 0, 46, this.boxWidth, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
            int color = 14737632;


            if (packedFGColour != 0) {
                color = packedFGColour;
            }
            if (this.isChecked)
                this.drawCenteredString(mc.fontRenderer, "√", this.x + this.boxWidth / 2, this.y + 3, 14737632);

            this.drawString(mc.fontRenderer, displayString, this.x + this.boxWidth + 2, this.y + 2, color);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        this.isChecked = !isChecked;
    }
}
