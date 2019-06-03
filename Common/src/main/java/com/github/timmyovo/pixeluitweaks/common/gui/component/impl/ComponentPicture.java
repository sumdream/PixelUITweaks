package com.github.timmyovo.pixeluitweaks.common.gui.component.impl;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;

import java.util.UUID;

public class ComponentPicture extends AbstractComponent {

    private ComponentPicture(Builder builder) {
        setComponentId(builder.componentId);
        xPos = builder.xPos;
        yPos = builder.yPos;
        setHeight(builder.height);
        setWidth(builder.width);
        setVisible(builder.visible);
        setRenderMethod(builder.renderMethod);
        setTextureBinder(builder.textureBinder);
        setContentHover(builder.contentHover);
        if (getComponentId() == null) {
            setComponentId(UUID.randomUUID());
        }
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

        public ComponentPicture build() {
            return new ComponentPicture(this);
        }
    }
}
