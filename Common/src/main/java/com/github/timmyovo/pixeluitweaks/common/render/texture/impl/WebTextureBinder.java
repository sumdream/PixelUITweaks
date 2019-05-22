package com.github.timmyovo.pixeluitweaks.common.render.texture.impl;

import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebTextureBinder extends TextureBinder {
    private String url;

    private WebTextureBinder(Builder builder) {
        setUrl(builder.url);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static final class Builder {
        private String url;

        private Builder() {
        }

        public Builder withUrl(String val) {
            url = val;
            return this;
        }

        public WebTextureBinder build() {
            return new WebTextureBinder(this);
        }
    }
}
