package com.github.timmyovo.pixeluitweaks.client.packet.in;

import com.github.timmyovo.pixeluitweaks.client.PixelUITweaks;
import com.github.timmyovo.pixeluitweaks.client.packet.IPacketIn;
import com.github.timmyovo.pixeluitweaks.client.texture.IDownloadTexture;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Map;

public class PacketInRecvTexture implements IPacketIn {
    private static final Map<String, byte[]> PROCESSING_TEXTURES = Maps.newConcurrentMap();

    private static byte[] combineByteArray(byte[] b1, byte[] b2) {
        byte[] combined = new byte[b1.length + b2.length];

        System.arraycopy(b1, 0, combined, 0, b1.length);
        System.arraycopy(b2, 0, combined, b1.length, b2.length);
        return combined;
    }

    @Override
    public void readPacket(PacketBuffer packetBuffer) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            String name = packetBuffer.readString(32);
            byte b = packetBuffer.readByte();
            if (b == 0) {
                byte[] bytesRecv = packetBuffer.readByteArray();
                byte[] bytes = PROCESSING_TEXTURES.get(name);
                if (bytes == null) {
                    PROCESSING_TEXTURES.put(name, bytesRecv);
                } else {
                    PROCESSING_TEXTURES.put(name, combineByteArray(bytes, bytesRecv));
                }
            }
            if (b == 1) {
                byte[] buf = PROCESSING_TEXTURES.get(name);
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                BufferedImage bufferedImage = null;
                try {
                    bufferedImage = ImageIO.read(byteArrayInputStream);
                    byteArrayInputStream.close();
                    PROCESSING_TEXTURES.remove(name);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                PixelUITweaks.INSTANCE.getDownloadTextureManager().getTextureMap().put(name, new IDownloadTexture(bufferedImage));
            }

        });
    }
}
