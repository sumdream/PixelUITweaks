package com.github.timmyovo.pixeluitweaks.client.gui.component;

import com.github.timmyovo.pixeluitweaks.client.gui.ClientComponent;
import com.github.timmyovo.pixeluitweaks.client.utils.TextureUtils;
import com.github.timmyovo.pixeluitweaks.common.gui.component.impl.*;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GuiListContentImpl extends Gui implements ClientComponent<ComponentListContent> {
    /**
     * The height of a slot.
     */
    public int slotHeight;
    public int width;
    public int height;
    /**
     * The top of the slot container. Affects the overlays and scrolling.
     */
    public int top;
    /**
     * The bottom of the slot container. Affects the overlays and scrolling.
     */
    public int bottom;
    public int right;
    public int left;
    public int headerPadding;
    protected Minecraft mc;
    protected int mouseX;
    protected int mouseY;
    protected boolean centerListVertically = true;
    /**
     * Where the mouse was in the window when you first clicked to scroll
     */
    protected int initialClickY = -2;
    /**
     * What to multiply the amount you moved your mouse by (used for slowing down scrolling when over the items and not
     * on the scroll bar)
     */
    protected float scrollMultiplier;
    /**
     * How far down this slot has been scrolled
     */
    protected float amountScrolled;
    /**
     * The element in the list that was selected
     */
    protected int selectedElement = -1;
    /**
     * The time when this button was last clicked.
     */
    protected long lastClicked;
    protected boolean visible = true;
    /**
     * Set to true if a selected element in this gui will show an outline box
     */
    protected boolean showSelectionBox = true;
    protected boolean hasListHeader;
    private ComponentListContent componentListContent;
    /**
     * The buttonID of the button used to scroll up
     */
    private int scrollUpButtonID;
    /**
     * The buttonID of the button used to scroll down
     */
    private int scrollDownButtonID;
    private boolean enabled = true;
    private int scrollbarX;
    private int overlayY;

    private List<ClientComponent> clientComponents;

    public GuiListContentImpl(ComponentListContent componentListContent) {
        this.componentListContent = componentListContent;
        updateComponent(componentListContent);
    }

    public void setDimensions(int widthIn, int heightIn, int topIn, int bottomIn) {
        this.width = widthIn;
        this.height = heightIn;
        this.top = topIn;
        this.bottom = bottomIn;
        this.left = 0;
        this.right = widthIn;
    }

    public void setShowSelectionBox(boolean showSelectionBoxIn) {
        this.showSelectionBox = showSelectionBoxIn;
    }

    /**
     * Sets hasListHeader and headerHeight. Params: hasListHeader, headerHeight. If hasListHeader is false headerHeight
     * is set to 0.
     */
    protected void setHasListHeader(boolean hasListHeaderIn, int headerPaddingIn) {
        this.hasListHeader = hasListHeaderIn;
        this.headerPadding = headerPaddingIn;

        if (!hasListHeaderIn) {
            this.headerPadding = 0;
        }
    }

    protected int getSize() {
        return clientComponents.size();
    }

    /**
     * The element in the slot that was clicked, boolean for whether it was double clicked or not
     */
    protected void elementClicked0(int slotIndex, boolean isDoubleClick, int mouseX, int mouseY) {
        //todo send event to server
        ClientComponent clientComponent = clientComponents.get(slotIndex);
        if (clientComponent.mousePressed(Minecraft.getMinecraft(), mouseX, mouseY)) {
            clientComponent.mouseReleased(mouseX, mouseY);
        }
    }

    /**
     * Returns true if the element passed in is currently selected
     */
    protected boolean isSelected(int slotIndex) {
        return selectedElement == slotIndex;
    }

    /**
     * Return the height of the content being scrolled
     */
    protected int getContentHeight() {
        return this.getSize() * this.slotHeight + this.headerPadding;
    }

    protected void drawBackground() {
        //todo drawBackground
    }

    protected void updateItemPos(int entryID, int insideLeft, int yPos, float partialTicks) {
    }

    protected void drawSlot(int slotIndex, int xPos, int yPos, int heightIn, int mouseXIn, int mouseYIn, float partialTicks) {
        ClientComponent clientComponent = clientComponents.get(slotIndex);
        if (clientComponent instanceof GuiButtonImpl) {
            GuiButtonImpl guiButton = (GuiButtonImpl) clientComponent;
            guiButton.setVisible(yPos <= this.height - guiButton.height && yPos >= overlayY);
            guiButton.setX(xPos);
            guiButton.setY(yPos);
            guiButton.setHeight(heightIn);
        }
        if (clientComponent instanceof GuiCheckBoxImpl) {
            GuiCheckBoxImpl guiCheckbox = (GuiCheckBoxImpl) clientComponent;
            guiCheckbox.setVisible(yPos <= this.height - guiCheckbox.height && yPos >= overlayY);
            guiCheckbox.setX(xPos);
            guiCheckbox.setY(yPos);
            guiCheckbox.setHeight(heightIn);
        }
        if (clientComponent instanceof GuiLabelImpl) {
            GuiLabelImpl guiLabel = (GuiLabelImpl) clientComponent;
            guiLabel.setVisible(yPos <= this.height - guiLabel.height && yPos >= overlayY);
            guiLabel.setX(xPos);
            guiLabel.setY(yPos);
            guiLabel.setHeight(heightIn);
        }
        if (clientComponent instanceof GuiTextFieldImpl) {
            GuiTextFieldImpl textfield = (GuiTextFieldImpl) clientComponent;
            textfield.setVisible(yPos <= this.height - textfield.height && yPos >= overlayY);
            textfield.setX(xPos);
            textfield.setY(yPos);
            textfield.setHeight(heightIn);
        }
        clientComponent.render(mouseX, mouseY, (int) partialTicks);

    }

    protected void drawHoveringText(List<String> textLines, int x, int y, int width, int height, FontRenderer font) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
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
            GlStateManager.enableDepth();
            GlStateManager.enableRescaleNormal();
        }
    }

    /**
     * Handles drawing a list's header row.
     */
    protected void drawListHeader(int insideLeft, int insideTop, Tessellator tessellatorIn) {
    }

    protected void clickedHeader(int p_148132_1_, int p_148132_2_) {
    }

    protected void renderDecorations(int mouseXIn, int mouseYIn) {
    }

    public int getSlotIndexFromScreenCoords(int posX, int posY) {
        int i = this.left + this.width / 2 - this.getListWidth() / 2;
        int j = this.left + this.width / 2 + this.getListWidth() / 2;
        int k = posY - this.top - this.headerPadding + (int) this.amountScrolled - 4;
        int l = k / this.slotHeight;
        return posX < this.getScrollBarX() && posX >= i && posX <= j && l >= 0 && k >= 0 && l < this.getSize() ? l : -1;
    }

    /**
     * Registers the IDs that can be used for the scrollbar's up/down buttons.
     */
    public void registerScrollButtons(int scrollUpButtonIDIn, int scrollDownButtonIDIn) {
        this.scrollUpButtonID = scrollUpButtonIDIn;
        this.scrollDownButtonID = scrollDownButtonIDIn;
    }

    /**
     * Stop the thing from scrolling out_ of bounds
     */
    protected void bindAmountScrolled() {
        this.amountScrolled = MathHelper.clamp(this.amountScrolled, 0.0F, (float) this.getMaxScroll());
    }

    public int getMaxScroll() {
        return Math.max(0, this.getContentHeight() - (this.bottom - this.top - 4));
    }

    /**
     * Returns the amountScrolled field as an integer.
     */
    public int getAmountScrolled() {
        return (int) this.amountScrolled;
    }

    public boolean isMouseYWithinSlotBounds(int p_148141_1_) {
        return p_148141_1_ >= this.top && p_148141_1_ <= this.bottom && this.mouseX >= this.left && this.mouseX <= this.right;
    }

    /**
     * Scrolls the slot by the given amount. A positive value scrolls down, and a negative value scrolls up.
     */
    public void scrollBy(int amount) {
        this.amountScrolled += (float) amount;
        this.bindAmountScrolled();
        this.initialClickY = -2;
    }

    public void actionPerformed(net.minecraft.client.gui.GuiButton button) {
        if (button.enabled) {
            if (button.id == this.scrollUpButtonID) {
                this.amountScrolled -= (float) (this.slotHeight * 2 / 3);
                this.initialClickY = -2;
                this.bindAmountScrolled();
            } else if (button.id == this.scrollDownButtonID) {
                this.amountScrolled += (float) (this.slotHeight * 2 / 3);
                this.initialClickY = -2;
                this.bindAmountScrolled();
            }
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        getClientComponents().stream()
                .filter(iServerGuiBase -> iServerGuiBase instanceof GuiTextFieldImpl)
                .forEach(clientComponent -> ((GuiTextFieldImpl) clientComponent).textboxKeyTyped(typedChar, keyCode));
    }

    @Override
    public void render(int mouseX, int mouseY, float ticks) {
        drawScreen(mouseX, mouseY, ticks);
    }

    @Override
    public void updateComponent(ComponentListContent componentModel) {
        Minecraft minecraft = Minecraft.getMinecraft();
        this.mc = minecraft;
        ScaledResolution scaledResolution = new ScaledResolution(minecraft);
        int scaledHeight = scaledResolution.getScaledHeight();
        int scaledWidth = scaledResolution.getScaledWidth();

        this.slotHeight = componentListContent.getSlotHeight();
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
        this.top = (int) new ExpressionBuilder(componentModel.getTop())
                .variables("w", "h")
                .build()
                .setVariable("w", scaledWidth)
                .setVariable("h", scaledHeight)
                .evaluate();
        this.bottom = (int) new ExpressionBuilder(componentModel.getBottom())
                .variables("w", "h")
                .build()
                .setVariable("w", scaledWidth)
                .setVariable("h", scaledHeight)
                .evaluate();
        this.right = (int) new ExpressionBuilder(componentModel.getRight())
                .variables("w", "h")
                .build()
                .setVariable("w", scaledWidth)
                .setVariable("h", scaledHeight)
                .evaluate();
        this.left = (int) new ExpressionBuilder(componentModel.getLeft())
                .variables("w", "h")
                .build()
                .setVariable("w", scaledWidth)
                .setVariable("h", scaledHeight)
                .evaluate();
        this.headerPadding = componentListContent.getHeaderPadding();
        this.visible = componentListContent.isVisible();
        this.scrollbarX = (int) new ExpressionBuilder(componentModel.getScrollbarX())
                .variables("w", "h")
                .build()
                .setVariable("w", scaledWidth)
                .setVariable("h", scaledHeight)
                .evaluate();
        this.overlayY = (int) new ExpressionBuilder(componentModel.getOverlayY())
                .variables("w", "h")
                .build()
                .setVariable("w", scaledWidth)
                .setVariable("h", scaledHeight)
                .evaluate();
        this.clientComponents = new ArrayList<>();
        this.componentListContent.getContents().forEach(abstractComponent -> {
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

    public void drawScreen(int mouseXIn, int mouseYIn, float partialTicks) {
        if (this.visible) {
            this.mouseX = mouseXIn;
            this.mouseY = mouseYIn;
            this.drawBackground();
            int i = this.getScrollBarX();
            int j = i + 6;
            this.bindAmountScrolled();
            GlStateManager.disableLighting();
            GlStateManager.disableFog();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuffer();
            // Forge: background rendering moved into separate method.
            this.drawContainerBackground(tessellator);
            int k = this.left + 5;
            int l = this.top + 4 - (int) this.amountScrolled;

            if (this.hasListHeader) {
                this.drawListHeader(k, l, tessellator);
            }

            this.drawSelectionBox(k, l, mouseXIn, mouseYIn, partialTicks);
            GlStateManager.disableDepth();
            this.overlayBackground(overlayY, this.top, 255, 255);
            this.overlayBackground(this.bottom, this.height, 255, 255);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
            GlStateManager.disableAlpha();
            GlStateManager.shadeModel(7425);
            GlStateManager.disableTexture2D();
            int i1 = 4;
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos((double) this.left, (double) (this.top + 4), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.right, (double) (this.top + 4), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.right, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.left, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
            tessellator.draw();
            bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            bufferbuilder.pos((double) this.left, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.right, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
            bufferbuilder.pos((double) this.right, (double) (this.bottom - 4), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            bufferbuilder.pos((double) this.left, (double) (this.bottom - 4), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 0).endVertex();
            tessellator.draw();
            int j1 = this.getMaxScroll();

            if (j1 > 0) {
                int k1 = (this.bottom - this.top) * (this.bottom - this.top) / this.getContentHeight();
                k1 = MathHelper.clamp(k1, 32, this.bottom - this.top - 8);
                int l1 = (int) this.amountScrolled * (this.bottom - this.top - k1) / j1 + this.top;

                if (l1 < this.top) {
                    l1 = this.top;
                }

                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) this.bottom, 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.bottom, 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) j, (double) this.top, 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) i, (double) this.top, 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) (l1 + k1), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j, (double) l1, 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                tessellator.draw();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i, (double) (l1 + k1 - 1), 0.0D).tex(0.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) (l1 + k1 - 1), 0.0D).tex(1.0D, 1.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) (j - 1), (double) l1, 0.0D).tex(1.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                bufferbuilder.pos((double) i, (double) l1, 0.0D).tex(0.0D, 0.0D).color(192, 192, 192, 255).endVertex();
                tessellator.draw();
            }

            this.renderDecorations(mouseXIn, mouseYIn);
            GlStateManager.enableTexture2D();
            GlStateManager.shadeModel(7424);
            GlStateManager.enableAlpha();
            GlStateManager.disableBlend();
        }
    }

    public void handleMouseInput() {
        if (this.isMouseYWithinSlotBounds(this.mouseY)) {
            if (Mouse.getEventButton() == 0 && Mouse.getEventButtonState() && this.mouseY >= this.top && this.mouseY <= this.bottom) {
                int i = (this.width - this.getListWidth()) / 2;
                int j = (this.width + this.getListWidth()) / 2;
                int k = this.mouseY - this.top - this.headerPadding + (int) this.amountScrolled - 4;
                int l = k / this.slotHeight;

                if (l < this.getSize() && this.mouseX >= i && this.mouseX <= j && l >= 0 && k >= 0) {
                    this.elementClicked(l, false, this.mouseX, this.mouseY);
                    this.selectedElement = l;
                } else if (this.mouseX >= i && this.mouseX <= j && k < 0) {
                    this.clickedHeader(this.mouseX - i, this.mouseY - this.top + (int) this.amountScrolled - 4);
                }
            }

            if (Mouse.isButtonDown(0) && this.getEnabled()) {
                getClientComponents().stream()
                        .filter(componentListContent -> componentListContent instanceof GuiTextFieldImpl)
                        .forEach(iServerGuiBase -> ((GuiTextFieldImpl) iServerGuiBase).mouseClicked(mouseX, mouseY, 0));
                if (this.initialClickY == -1) {
                    boolean flag1 = true;

                    if (this.mouseY >= this.top && this.mouseY <= this.bottom) {
                        int j2 = (this.width - this.getListWidth()) / 2;
                        int k2 = (this.width + this.getListWidth()) / 2;
                        int l2 = this.mouseY - this.top - this.headerPadding + (int) this.amountScrolled - 4;
                        int i1 = l2 / this.slotHeight;

                        if (i1 < this.getSize() && this.mouseX >= j2 && this.mouseX <= k2 && i1 >= 0 && l2 >= 0) {
                            boolean flag = i1 == this.selectedElement && Minecraft.getSystemTime() - this.lastClicked < 250L;
                            this.elementClicked0(i1, flag, this.mouseX, this.mouseY);
                            this.selectedElement = i1;
                            this.lastClicked = Minecraft.getSystemTime();
                        } else if (this.mouseX >= j2 && this.mouseX <= k2 && l2 < 0) {
                            this.clickedHeader(this.mouseX - j2, this.mouseY - this.top + (int) this.amountScrolled - 4);
                            flag1 = false;
                        }

                        int i3 = this.getScrollBarX();
                        int j1 = i3 + 6;

                        if (this.mouseX >= i3 && this.mouseX <= j1) {
                            this.scrollMultiplier = -1.0F;
                            int k1 = this.getMaxScroll();

                            if (k1 < 1) {
                                k1 = 1;
                            }

                            int l1 = (int) ((float) ((this.bottom - this.top) * (this.bottom - this.top)) / (float) this.getContentHeight());
                            l1 = MathHelper.clamp(l1, 32, this.bottom - this.top - 8);
                            this.scrollMultiplier /= (float) (this.bottom - this.top - l1) / (float) k1;
                        } else {
                            this.scrollMultiplier = 1.0F;
                        }

                        if (flag1) {
                            this.initialClickY = this.mouseY;
                        } else {
                            this.initialClickY = -2;
                        }
                    } else {
                        this.initialClickY = -2;
                    }
                } else if (this.initialClickY >= 0) {
                    this.amountScrolled -= (float) (this.mouseY - this.initialClickY) * this.scrollMultiplier;
                    this.initialClickY = this.mouseY;
                }
            } else {
                this.initialClickY = -1;
            }

            int i2 = Mouse.getEventDWheel();

            if (i2 != 0) {
                if (i2 > 0) {
                    i2 = -1;
                } else if (i2 < 0) {
                    i2 = 1;
                }

                this.amountScrolled += (float) (i2 * this.slotHeight / 2);
            }
        }
    }

    protected void elementClicked(int l, boolean b, int mouseX, int mouseY) {

    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabledIn) {
        this.enabled = enabledIn;
    }

    /**
     * Gets the width of the list
     */
    public int getListWidth() {
        return 220;
    }

    /**
     * Draws the selection box around the selected slot element.
     */
    protected void drawSelectionBox(int insideLeft, int insideTop, int mouseXIn, int mouseYIn, float partialTicks) {
        int i = this.getSize();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();

        for (int j = 0; j < i; ++j) {
            int k = insideTop + j * this.slotHeight + this.headerPadding;
            int l = this.slotHeight - 4;

            if (k > this.bottom || k + l < this.top) {
                this.updateItemPos(j, insideLeft, k, partialTicks);
            }

            if (this.showSelectionBox && this.isSelected(j) && k <= this.height - l && k >= overlayY) {
                int i1 = this.left + 5;
                int j1 = this.left + this.width / 2 + this.getListWidth() / 2;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GlStateManager.disableTexture2D();
                bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
                bufferbuilder.pos((double) i1, (double) (k + l + 2), 0.0D).tex(0.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j1, (double) (k + l + 2), 0.0D).tex(1.0D, 1.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) j1, (double) (k - 2), 0.0D).tex(1.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) i1, (double) (k - 2), 0.0D).tex(0.0D, 0.0D).color(128, 128, 128, 255).endVertex();
                bufferbuilder.pos((double) (i1 + 1), (double) (k + l + 1), 0.0D).tex(0.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) (j1 - 1), (double) (k + l + 1), 0.0D).tex(1.0D, 1.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) (j1 - 1), (double) (k - 1), 0.0D).tex(1.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                bufferbuilder.pos((double) (i1 + 1), (double) (k - 1), 0.0D).tex(0.0D, 0.0D).color(0, 0, 0, 255).endVertex();
                tessellator.draw();
                GlStateManager.enableTexture2D();
            }

            this.drawSlot(j, insideLeft, k, l, mouseXIn, mouseYIn, partialTicks);
        }
    }

    public int getScrollBarX() {
        return scrollbarX;
    }

    /**
     * Overlays the background to hide scrolled items
     */
    protected void overlayBackground(int startY, int endY, int startAlpha, int endAlpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        if (componentListContent.getTextureBinder() != null) {
            TextureUtils.tryBindTexture(componentListContent.getTextureBinder());
        } else {
            mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        bufferbuilder.pos((double) this.left, (double) endY, 0.0D).tex(0.0D, (double) ((float) endY / 32.0F)).color(64, 64, 64, endAlpha).endVertex();
        bufferbuilder.pos((double) (this.left + this.width), (double) endY, 0.0D).tex((double) ((float) this.width / 32.0F), (double) ((float) endY / 32.0F)).color(64, 64, 64, endAlpha).endVertex();
        bufferbuilder.pos((double) (this.left + this.width), (double) startY, 0.0D).tex((double) ((float) this.width / 32.0F), (double) ((float) startY / 32.0F)).color(64, 64, 64, startAlpha).endVertex();
        bufferbuilder.pos((double) this.left, (double) startY, 0.0D).tex(0.0D, (double) ((float) startY / 32.0F)).color(64, 64, 64, startAlpha).endVertex();
        tessellator.draw();
    }

    /**
     * Sets the left and right bounds of the slot. Param is the left bound, right is calculated as left + width.
     */
    public void setSlotXBoundsFromLeft(int leftIn) {
        this.left = leftIn;
        this.right = leftIn + this.width;
    }

    public int getSlotHeight() {
        return this.slotHeight;
    }

    protected void drawContainerBackground(Tessellator tessellator) {
        BufferBuilder buffer = tessellator.getBuffer();
        if (componentListContent.getTextureBinder() != null) {
            TextureUtils.tryBindTexture(componentListContent.getTextureBinder());
        } else {
            mc.getTextureManager().bindTexture(Gui.OPTIONS_BACKGROUND);
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32.0F;
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        buffer.pos((double) this.left, (double) this.bottom, 0.0D).tex((double) ((float) this.left / f), (double) ((float) (this.bottom + (int) this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
        buffer.pos((double) this.right, (double) this.bottom, 0.0D).tex((double) ((float) this.right / f), (double) ((float) (this.bottom + (int) this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
        buffer.pos((double) this.right, (double) this.top, 0.0D).tex((double) ((float) this.right / f), (double) ((float) (this.top + (int) this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
        buffer.pos((double) this.left, (double) this.top, 0.0D).tex((double) ((float) this.left / f), (double) ((float) (this.top + (int) this.amountScrolled) / f)).color(32, 32, 32, 255).endVertex();
        tessellator.draw();
    }

    public void notifyResolutionChanged(int width, int height) {
        final float standardPixels = 800 * 600;
        final float scaledSize = (width * height) / standardPixels;

    }
}
