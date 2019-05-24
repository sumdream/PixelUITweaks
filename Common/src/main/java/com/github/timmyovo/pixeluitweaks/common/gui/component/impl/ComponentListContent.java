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
    public String top;
    @SerializedName("bottom")
    public String bottom;
    @SerializedName("right")
    public String right;
    @SerializedName("left")
    public String left;
    @SerializedName("headerPadding")
    public int headerPadding;
    @SerializedName("scrollbarX")
    private String scrollbarX;
    @SerializedName("overlayY")
    private String overlayY;
    @SerializedName("contents")
    private List<AbstractComponent> contents;

    private ComponentListContent(Builder builder) {
        setComponentId(builder.componentId);
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
        private UUID componentId;
        private String xPos;
        private String yPos;
        private String height;
        private String width;
        private boolean visible;
        private RenderMethod renderMethod;
        private TextureBinder textureBinder;
        private ContentHover contentHover;
        private int slotHeight;
        private String top;
        private String bottom;
        private String right;
        private String left;
        private int headerPadding;
        private String scrollbarX;
        private String overlayY;
        private List<AbstractComponent> contents;

        private Builder() {
        }

        public Builder withComponentId(UUID val) {
            componentId = val;
            return this;
        }

        public Builder withXPos(String val) {
            xPos = val;
            return this;
        }

        public Builder withYPos(String val) {
            yPos = val;
            return this;
        }

        public Builder withHeight(String val) {
            height = val;
            return this;
        }

        public Builder withWidth(String val) {
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

        public Builder withTop(String val) {
            top = val;
            return this;
        }

        public Builder withBottom(String val) {
            bottom = val;
            return this;
        }

        public Builder withRight(String val) {
            right = val;
            return this;
        }

        public Builder withLeft(String val) {
            left = val;
            return this;
        }

        public Builder withHeaderPadding(int val) {
            headerPadding = val;
            return this;
        }

        public Builder withScrollbarX(String val) {
            scrollbarX = val;
            return this;
        }

        public Builder withOverlayY(String val) {
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
