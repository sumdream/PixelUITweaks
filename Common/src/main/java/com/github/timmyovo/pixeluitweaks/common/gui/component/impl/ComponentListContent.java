package com.github.timmyovo.pixeluitweaks.common.gui.component.impl;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ComponentListContent extends AbstractComponent {
    @SerializedName("slotHeight")
    public int slotHeight;
    @SerializedName("top")
    public int top;
    @SerializedName("bottom")
    public int bottom;
    @SerializedName("right")
    public int right;
    @SerializedName("left")
    public int left;
    @SerializedName("headerPadding")
    public int headerPadding;
    @SerializedName("scrollbarX")
    private int scrollbarX;
    @SerializedName("overlayY")
    private int overlayY;
    @SerializedName("contents")
    private List<AbstractComponent> contents;

    private ComponentListContent(Builder builder) {
        xPos = builder.xPos;
        yPos = builder.yPos;
        setHeight(builder.height);
        setWidth(builder.width);
        setVisible(builder.visible);
        setRenderMethod(builder.renderMethod);
        setTextureBinder(builder.textureBinder);
        setContentHover(builder.contentHover);
        setSlotHeight(builder.slotHeight);
        setTop(builder.top);
        setBottom(builder.bottom);
        setRight(builder.right);
        setLeft(builder.left);
        setHeaderPadding(builder.headerPadding);
        setScrollbarX(builder.scrollbarX);
        setOverlayY(builder.overlayY);
        setContents(builder.contents);
        setComponentId(UUID.randomUUID());
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private int xPos;
        private int yPos;
        private int height;
        private int width;
        private boolean visible;
        private RenderMethod renderMethod;
        private TextureBinder textureBinder;
        private ContentHover contentHover;
        private int slotHeight;
        private int top;
        private int bottom;
        private int right;
        private int left;
        private int headerPadding;
        private int scrollbarX;
        private int overlayY;
        private List<AbstractComponent> contents;

        private Builder() {
        }

        public Builder withXPos(int val) {
            xPos = val;
            return this;
        }

        public Builder withYPos(int val) {
            yPos = val;
            return this;
        }

        public Builder withHeight(int val) {
            height = val;
            return this;
        }

        public Builder withWidth(int val) {
            width = val;
            return this;
        }

        public Builder withVisible(boolean val) {
            visible = val;
            return this;
        }

        public Builder withRenderMethod(RenderMethod val) {
            renderMethod = val;
            return this;
        }

        public Builder withTextureBinder(TextureBinder val) {
            textureBinder = val;
            return this;
        }

        public Builder withContentHover(ContentHover val) {
            contentHover = val;
            return this;
        }

        public Builder withSlotHeight(int val) {
            slotHeight = val;
            return this;
        }

        public Builder withTop(int val) {
            top = val;
            return this;
        }

        public Builder withBottom(int val) {
            bottom = val;
            return this;
        }

        public Builder withRight(int val) {
            right = val;
            return this;
        }

        public Builder withLeft(int val) {
            left = val;
            return this;
        }

        public Builder withHeaderPadding(int val) {
            headerPadding = val;
            return this;
        }

        public Builder withScrollbarX(int val) {
            scrollbarX = val;
            return this;
        }

        public Builder withOverlayY(int val) {
            overlayY = val;
            return this;
        }

        public Builder withContents(List<AbstractComponent> val) {
            contents = val;
            return this;
        }

        public ComponentListContent build() {
            return new ComponentListContent(this);
        }
    }
}
