package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.client.gui.ClientRenderMethod;
import com.github.timmyovo.pixeluitweaks.client.packet.out_.PacketOutEvent;
import com.github.timmyovo.pixeluitweaks.client.utils.TextureUtils;
import com.github.timmyovo.pixeluitweaks.common.event.GuiEventType;
import com.github.timmyovo.pixeluitweaks.common.event.type.ButtonClickModel;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentButton;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.objecthunter.exp4j.ExpressionBuilder;

@Getter
@Setter
public class GuiButtonImpl extends Gui implements ClientComponent<ComponentButton> {
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
    private ComponentButton componentButton;

    private ClientRenderMethod clientRenderMethod;

    public GuiButtonImpl() {
    }

    public GuiButtonImpl(ComponentButton componentButton) {
        this.componentButton = componentButton;
        updateComponent(componentButton);
    }

    /**
     * Returns 0 if the button is disabled, 1 if the mouse is NOT hovering over this button and 2 if it IS hovering over
     * this button.
     */
    protected int getHoverState(boolean mouseOver) {
        int i = 1;

        if (mouseOver) {
            i = 2;
        }

        return i;
    }

    @Override
    public void render(int mouseX, int mouseY, float ticks) {
        if (visible) {
            FontRenderer fontrenderer = mc.fontRenderer;
            if (componentButton.getTextureBinder() == null) {
                mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            } else {
                TextureUtils.tryBindTexture(componentButton.getTextureBinder());
            }
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            if (componentButton.getRenderMethod() == null) {
                this.drawTexturedModalRect(this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
                this.drawTexturedModalRect(this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
            } else {
                for (ClientRenderMethod.ClientRenderEntry renderEntry : clientRenderMethod.getEntryList()) {
                    drawModalRectWithCustomSizedTexture(renderEntry.getXOffset(), renderEntry.getYOffset(), renderEntry.getTextureX(), renderEntry.getTextureY(), renderEntry.getScaledWidth(), renderEntry.getScaledHeight(), renderEntry.getTextureWidth(), renderEntry.getTextureHeight());
                }
            }
            int j = 14737632;

            if (packedFGColour != 0) {
                j = packedFGColour;
            } else if (this.hovered) {
                j = 16777120;
            }

            this.drawCenteredString(fontrenderer, this.displayString, this.x + this.width / 2, this.y + (this.height - 8) / 2, j);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        return this.visible && mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        PacketOutEvent.notifyEvent(GuiEventType.BUTTON_CLICK, ButtonClickModel.builder()
                .componentButton(componentButton)
                .build());
    }

    @Override
    public boolean isMouseOver() {
        return hovered;
    }

    @Override
    public ComponentButton getComponentModel() {
        return componentButton;
    }

    @Override
    public void updateComponent(ComponentButton componentModel) {
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
        if (componentModel.getRenderMethod() != null) {
            this.clientRenderMethod = ClientRenderMethod.fromRenderMethod(componentModel.getRenderMethod());
        }
        this.displayString = componentButton.getDisplayString();
        this.visible = componentButton.isVisible();
    }
}
