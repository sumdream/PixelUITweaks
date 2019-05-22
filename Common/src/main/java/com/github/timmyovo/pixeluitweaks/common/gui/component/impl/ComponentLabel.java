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
        xPos = builder.xPos;
        yPos = builder.yPos;
        setHeight(builder.height);
        setWidth(builder.width);
        setVisible(builder.visible);
        setRenderMethod(builder.renderMethod);
        setTextureBinder(builder.textureBinder);
        setContentHover(builder.contentHover);
        setCentered(builder.centered);
        setVisible(builder.visible);
        setTextColor(builder.textColor);
        setBorder(builder.border);
        setLabels(builder.labels);
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
        private int textColor;
        private int border;
        private List<String> labels;
        private RenderMethod renderMethod;
        private TextureBinder textureBinder;
        private ContentHover contentHover;
        private boolean centered;

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
    }
}
