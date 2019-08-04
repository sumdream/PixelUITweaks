package com.github.timmyovo.pixeluitweaks.client.gui;

import com.github.timmyovo.pixeluitweaks.client.gui.component.*;
import com.github.timmyovo.pixeluitweaks.client.packet.out_.PacketOutEvent;
import com.github.timmyovo.pixeluitweaks.client.utils.TextureUtils;
import com.github.timmyovo.pixeluitweaks.common.event.GuiEventType;
import com.github.timmyovo.pixeluitweaks.common.event.type.CloseScreenModel;
import com.github.timmyovo.pixeluitweaks.common.event.type.KeyboardInputModel;
import com.github.timmyovo.pixeluitweaks.common.event.type.MouseInputModel;
import com.github.timmyovo.pixeluitweaks.common.event.type.OpenScreenModel;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.*;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class CommonUIScreen extends GuiScreen {
    private EntityPlayer owner;
    private List<ComponentContainer> componentContainerList;
    private Set<ClientComponentContainer> containers;
    private List<ClientComponent> clientComponents;

    public CommonUIScreen(EntityPlayer entityPlayer, ComponentContainer... guiContainers) {
        this.owner = entityPlayer;
        this.componentContainerList = Arrays.stream(guiContainers).collect(Collectors.toList());
        this.containers = new HashSet<>();
        this.containers.addAll(Arrays.stream(guiContainers)
                .map(ClientComponentContainer::from)
                .collect(Collectors.toList()));
        for (ComponentContainer guiContainer : guiContainers) {
            addContainer(guiContainer);
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        PacketOutEvent.notifyEvent(GuiEventType.OPEN_SCREEN, OpenScreenModel.builder().screenContainers(componentContainerList).build());
    }

    public void addContainer(ComponentContainer componentContainer) {
        this.containers.add(ClientComponentContainer.from(componentContainer));
        updateClientComponents();
    }

    public void removeContainer(ComponentContainer componentContainer) {
        this.containers.removeIf(c -> c.getComponentContainer().equals(componentContainer));
        updateClientComponents();
    }

    public void updateClientComponents() {
        this.clientComponents = new ArrayList<>();
        for (ClientComponentContainer container : this.containers) {
            container.setClientRenderMethod(ClientRenderMethod.fromRenderMethod(container.getComponentContainer().getRenderMethod()));
            List<AbstractComponent> content = container.getComponentContainer().getComponentList();
            content.forEach(abstractComponent -> {
                if (abstractComponent instanceof ComponentButton) {
                    clientComponents.add(new GuiButtonImpl(((ComponentButton) abstractComponent)));
                    return;
                }
                if (abstractComponent instanceof ComponentLabel) {
                    clientComponents.add(new GuiLabelImpl(((ComponentLabel) abstractComponent)));
                    return;
                }
                if (abstractComponent instanceof ComponentPicture) {
                    clientComponents.add(new GuiPictureImpl(((ComponentPicture) abstractComponent)));
                    return;
                }
                if (abstractComponent instanceof ComponentCheckBox) {
                    clientComponents.add(new GuiCheckBoxImpl(((ComponentCheckBox) abstractComponent)));
                    return;
                }
                if (abstractComponent instanceof ComponentTextField) {
                    clientComponents.add(new GuiTextFieldImpl(((ComponentTextField) abstractComponent)));
                    return;
                }
                if (abstractComponent instanceof ComponentEntityRenderer) {
                    clientComponents.add(new GuiEntityRendererImpl(((ComponentEntityRenderer) abstractComponent)));
                    return;
                }
            });
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (ClientComponentContainer container : containers) {
            if (container.getComponentContainer().getTextureBinder() != null) {
                TextureUtils.tryBindTexture(container.getComponentContainer().getTextureBinder());
            }

            if (container.getClientRenderMethod() != null) {
                container.getClientRenderMethod().render();
            }
            for (AbstractComponent abstractComponent : container.getComponentContainer().getComponentList()) {
                if (!(abstractComponent instanceof ComponentSlot)) {
                    clientComponents.forEach(clientComponent -> {
                        clientComponent.render(mouseX, mouseY, partialTicks);

                    });
                    clientComponents.forEach(clientComponent -> {
                        ContentHover contentHover = clientComponent.getComponentModel().getContentHover();
                        if (clientComponent.isMouseOver() && contentHover != null) {
                            drawHoveringText(contentHover.getHoverTexts(), mouseX, mouseY, this.width, this.height, Minecraft.getMinecraft().fontRenderer);
                        }
                    });
                }
            }
        }
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height) {
        super.setWorldAndResolution(mc, width, height);
        updateClientComponents();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        Keyboard.enableRepeatEvents(true);
        clientComponents.stream()
                .filter(iServerGuiBase -> iServerGuiBase instanceof GuiTextFieldImpl)
                .map(iServerGuiBase -> ((GuiTextFieldImpl) iServerGuiBase))
                .forEach(guiTextfield -> guiTextfield.textboxKeyTyped(typedChar, keyCode));
        clientComponents.stream()
                .filter(iServerGuiBase -> iServerGuiBase instanceof GuiListContentImpl)
                .map(iServerGuiBase -> ((GuiListContentImpl) iServerGuiBase))
                .forEach(guiListContent -> guiListContent.keyTyped(typedChar, keyCode));
        PacketOutEvent.notifyEvent(GuiEventType.KEYBOARD_EVENT, KeyboardInputModel.builder()
                .inputEntry(KeyboardInputModel.InputEntry.builder()
                        .componentContainer(componentContainerList)
                        .keycode(keyCode)
                        .typedChar(typedChar)
                        .build())
                .build());
        if (clientComponents.stream()
                .filter(clientComponent -> clientComponent instanceof GuiTextFieldImpl)
                .map(clientComponent -> ((GuiTextFieldImpl) clientComponent))
                .anyMatch(GuiTextFieldImpl::isFocused)) {
            if (keyCode == Keyboard.KEY_E) {
                return;
            }
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clientComponents.stream()
                .filter(clientComponent -> clientComponent.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY))
                .forEach(clientComponent -> {
                    clientComponent.mouseReleased(mouseX, mouseY);
                });
        PacketOutEvent.notifyEvent(GuiEventType.MOUSE_EVENT, MouseInputModel.builder()
                .mouseEventDataModel(MouseInputModel.MouseEventDataModel.builder()
                        .componentContainer(componentContainerList)
                        .mouseButton(mouseButton)
                        .mouseX(mouseX)
                        .mouseY(mouseY)
                        .build())
                .build());
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        PacketOutEvent.notifyEvent(GuiEventType.CLOSE_SCREEN, CloseScreenModel.builder().screenContainers(componentContainerList).build());
        super.onGuiClosed();
    }

    protected void drawHoveringText(List<String> textLines, int x, int y, int width, int height, FontRenderer font) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            //RenderHelper.disableStandardItemLighting();
            //GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;

            for (String s : textLines) {
                int j = font.getStringWidth(s);

                if (j > i) {
                    i = j;
                }
            }

            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;

            if (textLines.size() > 1) {
                k += 2 + (textLines.size() - 1) * 10;
            }

            if (l1 + i > width) {
                l1 -= 28 + i;
            }

            if (i2 + k + 6 > height) {
                i2 = height - k - 6;
            }

            this.zLevel = 300.0F;
            int l = -267386864;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, -267386864, -267386864);
            this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, -267386864, -267386864);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, -267386864, -267386864);
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, -267386864, -267386864);
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, -267386864, -267386864);
            int i1 = 1347420415;
            int j1 = 1344798847;
            this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, 1347420415, 1344798847);
            this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, 1347420415, 1344798847);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, 1347420415, 1347420415);
            this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, 1344798847, 1344798847);

            for (int k1 = 0; k1 < textLines.size(); ++k1) {
                String s1 = textLines.get(k1);
                font.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

                if (k1 == 0) {
                    i2 += 2;
                }

                i2 += 10;
            }

            this.zLevel = 0.0F;
            //GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            //RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }


    public void drawComponent(AbstractComponent abstractComponent, int mouseX, int mouseY, float partialTicks) {
        if (abstractComponent instanceof ComponentButton) {
            new GuiButtonImpl(((ComponentButton) abstractComponent)).render(mouseX, mouseY, partialTicks);
            return;
        }
        if (abstractComponent instanceof ComponentLabel) {
            new GuiLabelImpl(((ComponentLabel) abstractComponent)).render(mouseX, mouseY, partialTicks);
            return;
        }
        if (abstractComponent instanceof ComponentPicture) {
            new GuiPictureImpl(((ComponentPicture) abstractComponent)).render(mouseX, mouseY, partialTicks);
        }
        if (abstractComponent instanceof ComponentCheckBox) {
            new GuiCheckBoxImpl(((ComponentCheckBox) abstractComponent)).render(mouseX, mouseY, partialTicks);
        }
        if (abstractComponent instanceof ComponentTextField) {
            new GuiTextFieldImpl(((ComponentTextField) abstractComponent)).render(mouseX, mouseY, partialTicks);
        }
    }
}
