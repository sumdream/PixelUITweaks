package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.client.gui.ClientItemRenderer;
import com.github.timmyovo.pixeluitweaks.client.gui.ClientRenderMethod;
import com.github.timmyovo.pixeluitweaks.client.utils.TextureUtils;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentPicture;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.objecthunter.exp4j.ExpressionBuilder;

@Getter
@Setter
public class GuiPictureImpl extends Gui implements ClientComponent<ComponentPicture> {
    private ComponentPicture componentPicture;
    private ClientRenderMethod clientRenderMethod;
    private boolean visible;
    private boolean hovered;
    public int width;
    public int height;
    public int x;
    public int y;
    private ClientItemRenderer clientItemRenderer;

    public GuiPictureImpl(ComponentPicture componentPicture) {
        this.componentPicture = componentPicture;
        updateComponent(componentPicture);
    }

    @Override
    public void render(int mouseX, int mouseY, float ticks) {
        if (visible) {
            this.hovered = mouseX >= x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            if (componentPicture.getTextureBinder() != null) {
                TextureUtils.tryBindTexture(componentPicture.getTextureBinder());
            }
            if (componentPicture.getRenderMethod() != null) {
                clientRenderMethod.render();
            }
            if (clientItemRenderer != null) {
                clientItemRenderer.render(ticks);
            }
        }
    }

    @Override
    public ComponentPicture getComponentModel() {
        return componentPicture;
    }

    @Override
    public boolean isMouseOver() {
        return hovered;
    }

    @Override
    public void updateComponent(ComponentPicture componentModel) {
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
        if (componentModel.getRenderMethod() != null) {
            this.clientRenderMethod = ClientRenderMethod.fromRenderMethod(componentModel.getRenderMethod());
        }
        this.visible = componentPicture.isVisible();
        if (componentModel.getItemRenderer() != null) {
            this.clientItemRenderer = ClientItemRenderer.fromItemRenderer(componentModel.getItemRenderer());
        }
    }
}
