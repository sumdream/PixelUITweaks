package com.github.timmyovo.pixeluitweaks.client.texture;

import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class DownloadTextureManager implements IComp<DownloadTextureManager> {
    private Map<String, IDownloadTexture> textureMap;


    @Override
    public DownloadTextureManager init() {
        textureMap = new HashMap<>();
        return this;
    }

    public IDownloadTexture getTexture(String entry) {
        return textureMap.get(entry);
    }
}
