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
public class ComponentLabel extends AbstractComponent {
    @SerializedName("centered")
    protected boolean centered;
    @SerializedName("textColor")
    protected int textColor;
    @SerializedName("border")
    private int border;
    @SerializedName("labels")
    private List<String> labels;

    private ComponentLabel(Builder builder) {
        setComponentId(builder.componentId);
        xPos = builder.xPos;
        yPos = builder.yPos;
        setHeight(builder.height);
        setWidth(builder.width);
        setVisible(builder.visible);
        setRenderMethod(builder.renderMethod);
        setTextureBinder(builder.textureBinder);
        setContentHover(builder.contentHover);
        setCentered(builder.centered);
        setTextColor(builder.textColor);
        setBorder(builder.border);
        setLabels(builder.labels);
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
        private boolean centered;
        private int textColor;
        private int border;
        private List<String> labels;

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

        public Builder withCentered(boolean val) {
            centered = val;
            return this;
        }

        public Builder withTextColor(int val) {
            textColor = val;
            return this;
        }

        public Builder withBorder(int val) {
            border = val;
            return this;
        }

        public Builder withLabels(List<String> val) {
            labels = val;
            return this;
        }

        public ComponentLabel build() {
            return new ComponentLabel(this);
        }
    }
}
