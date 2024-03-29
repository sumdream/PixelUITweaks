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
public class ComponentTextField extends AbstractComponent {
    @SerializedName("text")
    protected String text;
    @SerializedName("maxStringLength")
    protected int maxStringLength;
    @SerializedName("enableBackgroundDrawing")
    protected boolean enableBackgroundDrawing;
    @SerializedName("canLoseFocus")
    protected boolean canLoseFocus;

    private ComponentTextField(Builder builder) {
        setComponentId(builder.componentId);
        xPos = builder.xPos;
        yPos = builder.yPos;
        setHeight(builder.height);
        setWidth(builder.width);
        setVisible(builder.visible);
        setRenderMethod(builder.renderMethod);
        setTextureBinder(builder.textureBinder);
        setContentHover(builder.contentHover);
        setText(builder.text);
        setMaxStringLength(builder.maxStringLength);
        setEnableBackgroundDrawing(builder.enableBackgroundDrawing);
        setCanLoseFocus(builder.canLoseFocus);
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
        private String text;
        private int maxStringLength;
        private boolean enableBackgroundDrawing;
        private boolean canLoseFocus;

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

        public Builder withText(String val) {
            text = val;
            return this;
        }

        public Builder withMaxStringLength(int val) {
            maxStringLength = val;
            return this;
        }

        public Builder withEnableBackgroundDrawing(boolean val) {
            enableBackgroundDrawing = val;
            return this;
        }

        public Builder withCanLoseFocus(boolean val) {
            canLoseFocus = val;
            return this;
        }

        public ComponentTextField build() {
            return new ComponentTextField(this);
        }
    }
}
