package com.github.timmyovo.pixeluitweaks.common.gui.component.impl;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ComponentSlot extends AbstractComponent {
    @SerializedName("slotIndex")
    private int slotIndex;

    private ComponentSlot(Builder builder) {
        xPos = builder.xPos;
        yPos = builder.yPos;
        setHeight(builder.height);
        setWidth(builder.width);
        setVisible(builder.visible);
        setRenderMethod(builder.renderMethod);
        setTextureBinder(builder.textureBinder);
        setContentHover(builder.contentHover);
        setSlotIndex(builder.slotIndex);
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
        private int slotIndex;

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

        public Builder withSlotIndex(int val) {
            slotIndex = val;
            return this;
        }

        public ComponentSlot build() {
            return new ComponentSlot(this);
        }
    }
}
