package com.github.timmyovo.pixeluitweaks.server.config;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.image.WritableRenderedImage;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TextureConfiguration {
    public static Map<String, WritableRenderedImage> texturesMap = Maps.newHashMap();
    private List<String> textureList;
}
