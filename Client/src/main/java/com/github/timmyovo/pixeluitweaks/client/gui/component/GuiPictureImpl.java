package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.client.gui.ClientRenderMethod;
import com.github.timmyovo.pixeluitweaks.client.utils.TextureUtils;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.ComponentPicture;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Gui;

@Getter
@Setter
public class GuiPictureImpl extends Gui implements ClientComponent<ComponentPicture> {
    private ComponentPicture componentPicture;
    private ClientRenderMethod clientRenderMethod;
    private boolean visible;

    public GuiPictureImpl(ComponentPicture componentPicture) {
        this.componentPicture = componentPicture;

    }

    @Override
    public void render(int mouseX, int mouseY, float ticks) {
        if (visible) {
            if (componentPicture.getTextureBinder() != null) {
                TextureUtils.tryBindTexture(componentPicture.getTextureBinder());
            }
            if (componentPicture.getRenderMethod() != null) {
                for (ClientRenderMethod.ClientRenderEntry renderEntry : clientRenderMethod.getEntryList()) {
                    drawModalRectWithCustomSizedTexture(renderEntry.getXOffset(), renderEntry.getYOffset(), renderEntry.getTextureX(), renderEntry.getTextureY(), renderEntry.getScaledWidth(), renderEntry.getScaledHeight(), renderEntry.getTextureWidth(), renderEntry.getTextureHeight());
                }
            }
        }
    }

    @Override
    public void updateComponent(ComponentPicture componentModel) {
        this.clientRenderMethod = ClientRenderMethod.fromRenderMethod(componentModel.getRenderMethod());
        this.visible = componentPicture.isVisible();
    }
}
