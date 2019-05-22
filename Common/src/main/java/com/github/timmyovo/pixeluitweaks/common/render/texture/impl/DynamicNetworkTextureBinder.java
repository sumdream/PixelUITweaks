package com.github.timmyovo.pixeluitweaks.common.render.texture.impl;

import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamicNetworkTextureBinder extends TextureBinder {
    private String networkTextureName;

    private DynamicNetworkTextureBinder(Builder builder) {
        setNetworkTextureName(builder.networkTextureName);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String networkTextureName;

        private Builder() {
        }

        public Builder withNetworkTextureName(String val) {
            networkTextureName = val;
            return this;
        }

        public DynamicNetworkTextureBinder build() {
            return new DynamicNetworkTextureBinder(this);
        }
    }
}
