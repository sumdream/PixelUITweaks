package com.github.timmyovo.pixeluitweaks.common.render;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RenderMethod {
    @SerializedName("entryList")
    private List<RenderEntry> entryList;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RenderEntry {
        @SerializedName("xOffset")
        private String xOffset;
        @SerializedName("yOffset")
        private String yOffset;
        @SerializedName("textureX")
        private int textureX;
        @SerializedName("textureY")
        private int textureY;
        @SerializedName("textureWidth")
        private int textureWidth;
        @SerializedName("textureHeight")
        private int textureHeight;
        @SerializedName("scaledWidth")
        private String scaledWidth;
        @SerializedName("scaledHeight")
        private String scaledHeight;
    }
}
