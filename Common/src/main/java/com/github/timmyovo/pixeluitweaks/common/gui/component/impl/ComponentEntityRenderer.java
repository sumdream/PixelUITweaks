package com.github.timmyovo.pixeluitweaks.common.gui.component.impl;

import com.github.timmyovo.pixeluitweaks.common.gui.component.AbstractComponent;
import com.github.timmyovo.pixeluitweaks.common.gui.hover.ContentHover;
import com.github.timmyovo.pixeluitweaks.common.render.RenderMethod;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ComponentEntityRenderer extends AbstractComponent {

    private ComponentEntityRenderer(Builder builder) {
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

        public Builder() {
        }

        public Builder componentId(UUID val) {
            componentId = val;
            return this;
        }

        public Builder xPos(String val) {
            xPos = val;
            return this;
        }

        public Builder yPos(String val) {
            yPos = val;
            return this;
        }

        public Builder height(String val) {
            height = val;
            return this;
        }

        public Builder width(String val) {
            width = val;
            return this;
        }

        public Builder visible(boolean val) {
            visible = val;
            return this;
        }

        public Builder renderMethod(RenderMethod val) {
            renderMethod = val;
            return this;
        }

        public Builder textureBinder(TextureBinder val) {
            textureBinder = val;
            return this;
        }

        public Builder contentHover(ContentHover val) {
            contentHover = val;
            return this;
        }

        public ComponentEntityRenderer build() {
            return new ComponentEntityRenderer(this);
        }
    }
}
