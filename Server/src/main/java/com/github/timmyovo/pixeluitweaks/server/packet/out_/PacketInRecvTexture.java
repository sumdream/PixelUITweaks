package com.github.timmyovo.pixeluitweaks.server.packet.out_;

import com.github.timmyovo.pixeluitweaks.common.packet.PacketInTypes;
import com.github.timmyovo.pixeluitweaks.server.packet.IPacketOut;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PacketInRecvTexture implements IPacketOut {
    private String name;
    private BufferedImage image;

    public PacketInRecvTexture(String name, BufferedImage image) {
        this.name = name;
        this.image = image;
    }

    @Override
    public void writePacket(PacketDataSerializer packetBuffer) {
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "PNG", byteArrayOutputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        packetBuffer.a(name);
        packetBuffer.a(byteArrayOutputStream.toByteArray());
        try {
            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPacketType() {
        return PacketInTypes.PacketInRecvTexture.name();
    }
}
