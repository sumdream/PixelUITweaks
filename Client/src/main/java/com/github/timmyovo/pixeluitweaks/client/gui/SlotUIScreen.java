package com.github.timmyovo.pixeluitweaks.client.gui;

import com.github.timmyovo.pixeluitweaks.client.gui.component.*;
import com.github.timmyovo.pixeluitweaks.client.gui.container.CommonContainer;
import com.github.timmyovo.pixeluitweaks.client.utils.TextureUtils;
import com.github.timmyovo.pixeluitweaks.common.gui.ComponentContainer;
import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.*;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class SlotUIScreen extends GuiContainer {
    private EntityPlayer owner;
    private List<ComponentContainer> containers;
    private List<ClientComponent> clientComponents;

    public SlotUIScreen(EntityPlayer entityPlayer, ComponentContainer... guiContainers) {
        super(null);
        this.owner = entityPlayer;
        this.containers = Arrays.stream(guiContainers).collect(Collectors.toList());
        this.inventorySlots = new CommonContainer(
                Arrays.stream(guiContainers)
                        .map(ComponentContainer::getComponentList)
                        .filter(abstractComponents -> abstractComponents.stream().anyMatch(abstractComponent -> abstractComponent instanceof ComponentSlot))
                        .map(abstractComponents -> abstractComponents.stream().filter(abstractComponent -> abstractComponent instanceof ComponentSlot).map(abstractComponent -> ((ComponentSlot) abstractComponent)).collect(Collectors.toList()))
                        .collect(Collector.of(ArrayList::new, ArrayList::addAll, (left, right) -> {
                            left.addAll(right);
                            return left;
                        })), entityPlayer
        );
        for (ComponentContainer guiContainer : guiContainers) {
            addContainer(guiContainer);
        }
    }

    public void addContainer(ComponentContainer componentContainer) {
        this.containers.add(componentContainer);
        updateClientComponents();
    }

    public void removeContainer(ComponentContainer componentContainer) {
        this.containers.remove(componentContainer);
        updateClientComponents();
    }

    public void updateClientComponents() {
        this.clientComponents = new ArrayList<>();
        for (ComponentContainer container : this.containers) {
            List<AbstractComponent> content = container.getComponentList();
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
                }
                if (abstractComponent instanceof ComponentCheckBox) {
                    clientComponents.add(new GuiCheckBoxImpl(((ComponentCheckBox) abstractComponent)));
                }
                if (abstractComponent instanceof ComponentTextField) {
                    clientComponents.add(new GuiTextFieldImpl(((ComponentTextField) abstractComponent)));
                }
            });
        }
    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (ComponentContainer container : containers) {
            if (container.getTextureBinder() != null) {
                TextureUtils.tryBindTexture(container.getTextureBinder());
            }

            if (container.getRenderMethod() != null) {
                RenderMethod renderMethod = container.getRenderMethod();
                for (RenderMethod.RenderEntry renderEntry : renderMethod.getEntryList()) {
                    drawModalRectWithCustomSizedTexture(renderEntry.getXOffset(), renderEntry.getYOffset(), renderEntry.getTextureX(), renderEntry.getTextureY(), renderEntry.getScaledWidth(), renderEntry.getScaledHeight(), renderEntry.getTextureWidth(), renderEntry.getTextureHeight());
                }
            }
            for (AbstractComponent abstractComponent : container.getComponentList()) {
                if (!(abstractComponent instanceof ComponentSlot)) {
                    clientComponents.forEach(clientComponent -> {
                        clientComponent.render(mouseX, mouseY, partialTicks);
                        ContentHover hoverContent = clientComponent.getHoverContent();
                        if (clientComponent.isMouseOver() && hoverContent != null) {
                            drawHoveringText(hoverContent.getHoverTexts(), mouseX, mouseY, this.width, this.height, Minecraft.getMinecraft().fontRenderer);
                        }
                    });
                }
            }
        }
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
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        clientComponents.stream()
                .filter(iServerGuiBase -> iServerGuiBase instanceof GuiButtonImpl)
                .map(iServerGuiBase -> ((GuiButtonImpl) iServerGuiBase))
                .filter(guiButton -> guiButton.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY))
                .forEach(guiButton -> guiButton.mouseReleased(mouseX, mouseY));
        clientComponents.stream()
                .filter(iServerGuiBase -> iServerGuiBase instanceof GuiTextFieldImpl)
                .map(iServerGuiBase -> ((GuiTextFieldImpl) iServerGuiBase))
                .forEach(guiTextfield -> guiTextfield.setFocused(guiTextfield.mouseClicked(mouseX, mouseY, mouseButton)));
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }


    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }

    protected void drawHoveringText(List<String> textLines, int x, int y, int width, int height, FontRenderer font) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
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
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
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

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        for (ComponentContainer container : containers) {
            TextureUtils.tryBindTexture(container.getTextureBinder());
            RenderMethod renderMethod = container.getRenderMethod();
            for (RenderMethod.RenderEntry renderEntry : renderMethod.getEntryList()) {
                drawModalRectWithCustomSizedTexture(renderEntry.getXOffset(), renderEntry.getYOffset(), renderEntry.getTextureX(), renderEntry.getTextureY(), renderEntry.getScaledWidth(), renderEntry.getScaledHeight(), renderEntry.getTextureWidth(), renderEntry.getTextureHeight());
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {

    }
}
