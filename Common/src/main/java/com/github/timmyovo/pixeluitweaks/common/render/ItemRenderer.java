package com.github.timmyovo.pixeluitweaks.common.render;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRenderer {
    @SerializedName(value = "itemName")
    private String itemName;
    @SerializedName(value = "itemRendererMeta")
    private ItemRendererMeta itemRendererMeta;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ItemRendererMeta {
        private String x;
        private String y;
    }
}
