package com.github.timmyovo.pixeluitweaks.client.texture;

import lombok.Data;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

@Data
public class IDownloadTexture {
    private BufferedImage bufferedImage;
    private int width, height;
    private DynamicTexture dynamicTexture;
    private ResourceLocation resourceLocation;

    public IDownloadTexture(BufferedImage bufferedImage) {
        this(bufferedImage, false);
    }

    public IDownloadTexture(BufferedImage bufferedImage, boolean disableLoad) {
        this.bufferedImage = bufferedImage;
        if (!disableLoad) {
            load();
        }
    }

    public boolean hasLoad() {
        return resourceLocation != null;
    }

    public void load() {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        resourceLocation = textureManager.getDynamicTextureLocation(UUID.randomUUID().toString().substring(0, 16), new DynamicTexture(bufferedImage));
    }

    public void bind() {
        if (!hasLoad()) {
            return;
        }
        Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}