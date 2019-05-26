package com.github.timmyovo.pixeluitweaks.client.packet.in;

import com.github.timmyovo.pixeluitweaks.client.PixelUITweaks;
import com.github.timmyovo.pixeluitweaks.client.packet.IPacketIn;
import com.github.timmyovo.pixeluitweaks.client.texture.IDownloadTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class PacketInRecvTexture implements IPacketIn {
    @Override
    public void readPacket(PacketBuffer packetBuffer) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            String name = packetBuffer.readString(32);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packetBuffer.readByteArray());
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(byteArrayInputStream);
                byteArrayInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            PixelUITweaks.INSTANCE.getDownloadTextureManager().getTextureMap().put(name, new IDownloadTexture(bufferedImage));
        });
    }
}
