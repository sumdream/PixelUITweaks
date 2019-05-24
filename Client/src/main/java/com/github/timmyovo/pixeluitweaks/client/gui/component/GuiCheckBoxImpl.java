package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentCheckBox;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.objecthunter.exp4j.ExpressionBuilder;

@Getter
@Setter
public class GuiCheckBoxImpl extends Gui implements ClientComponent<ComponentCheckBox> {
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
    /**
     * Button width in pixels
     */
    public int width;
    /**
     * Button height in pixels
     */
    public int height;
    /**
     * The x position of this control.
     */
    public int x;
    /**
     * The y position of this control.
     */
    public int y;
    /**
     * The string displayed on this control.
     */
    public String displayString;
    /**
     * Hides the button completely if false.
     */
    public boolean visible;
    public int packedFGColour; //FML
    protected Minecraft mc;
    protected boolean hovered;
    protected int boxWidth;
    private ComponentCheckBox componentCheckBox;
    private boolean isChecked;

    public GuiCheckBoxImpl(ComponentCheckBox componentCheckBox) {
        this.componentCheckBox = componentCheckBox;
        updateComponent(componentCheckBox);
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

    @Override
    public void updateComponent(ComponentCheckBox componentModel) {
        this.mc = Minecraft.getMinecraft();
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
        this.displayString = componentModel.getDisplayString();
        this.boxWidth = componentModel.getBoxWidth();
        this.visible = componentModel.isVisible();
    }
}
