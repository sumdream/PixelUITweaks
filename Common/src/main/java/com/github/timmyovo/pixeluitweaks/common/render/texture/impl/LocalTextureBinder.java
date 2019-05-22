package com.github.timmyovo.pixeluitweaks.common.render.texture.impl;

import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocalTextureBinder extends TextureBinder {
    private String texturePath;

    private LocalTextureBinder(Builder builder) {
        setTexturePath(builder.texturePath);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String texturePath;

        private Builder() {
        }

        public Builder withTexturePath(String val) {
            texturePath = val;
            return this;
        }

        public LocalTextureBinder build() {
            return new LocalTextureBinder(this);
        }
    }
}
