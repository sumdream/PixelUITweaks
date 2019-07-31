package com.github.timmyovo.pixeluitweaks.client.utils;

import com.github.timmyovo.pixeluitweaks.client.PixelUITweaks;
import com.github.timmyovo.pixeluitweaks.client.texture.IDownloadTexture;
import com.github.timmyovo.pixeluitweaks.common.render.texture.TextureBinder;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.DynamicNetworkTextureBinder;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.LocalTextureBinder;
import com.github.timmyovo.pixeluitweaks.common.render.texture.impl.WebTextureBinder;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TextureUtils {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static volatile Map<String, Future<IDownloadTexture>> webImageCache = Maps.newHashMap();

    public static void tryBindTexture(@Nullable TextureBinder textureBinder) {
        if (textureBinder != null) {
            if (textureBinder instanceof DynamicNetworkTextureBinder) {
                IDownloadTexture texture = PixelUITweaks.INSTANCE.getDownloadTextureManager().getTexture(((DynamicNetworkTextureBinder) textureBinder).getNetworkTextureName());
                if (texture != null) {
                    texture.bind();
                }
                return;
            }
            if (textureBinder instanceof LocalTextureBinder) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(((LocalTextureBinder) textureBinder).getTexturePath()));
                return;
            }
            if (textureBinder instanceof WebTextureBinder) {
                tryBindWebImage(((WebTextureBinder) textureBinder).getUrl());
            }
        }
    }

    public static void tryBindWebImage(String string) {
        Future<IDownloadTexture> iDownloadTextureFuture = webImageCache.get(string);
        if (iDownloadTextureFuture == null) {
            webImageCache.put(string, executorService.submit(() -> {
                try {
                    URL url = new URL(string);
                    BufferedImage read = ImageIO.read(url);
                    if (read != null) {
                        return new IDownloadTexture(read);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }));
            return;
        }

        if (iDownloadTextureFuture.isDone()) {
            IDownloadTexture iDownloadTexture = null;
            try {
                iDownloadTexture = iDownloadTextureFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            if (iDownloadTexture != null) {
                iDownloadTexture.bind();
            }
        }

    }
}
