package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.client.utils.TextureUtils;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentPicture;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;

@Getter
@Setter
public class GuiPictureImpl extends Gui implements ClientComponent {
    private ComponentPicture componentPicture;
    private boolean visible;

    public GuiPictureImpl(ComponentPicture componentPicture) {
        this.componentPicture = componentPicture;
        this.visible = componentPicture.isVisible();
    }

    @Override
    public void render(int mouseX, int mouseY, float ticks) {
        if (visible) {
            if (componentPicture.getTextureBinder() != null) {
                TextureUtils.tryBindTexture(componentPicture.getTextureBinder());
            }
            if (componentPicture.getRenderMethod() != null) {
                RenderMethod renderMethod = componentPicture.getRenderMethod();
                for (RenderMethod.RenderEntry renderEntry : renderMethod.getEntryList()) {
                    drawModalRectWithCustomSizedTexture(renderEntry.getXOffset(), renderEntry.getYOffset(), renderEntry.getTextureX(), renderEntry.getTextureY(), renderEntry.getScaledWidth(), renderEntry.getScaledHeight(), renderEntry.getTextureWidth(), renderEntry.getTextureHeight());
                }
            }
        }
    }
}
