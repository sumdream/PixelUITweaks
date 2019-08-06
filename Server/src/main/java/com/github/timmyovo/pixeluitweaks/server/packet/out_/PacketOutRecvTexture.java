package com.github.timmyovo.pixeluitweaks.server.packet.out_;

import com.github.timmyovo.pixeluitweaks.common.packet.PacketTypes;
import com.github.timmyovo.pixeluitweaks.server.packet.IPacketOut;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import net.minecraft.server.v1_12_R1.PacketPlayOutCustomPayload;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class PacketOutRecvTexture implements IPacketOut {
    private String name;
    private BufferedImage image;
    private byte[] bytes;

    public PacketOutRecvTexture(String name, BufferedImage image) {
        this.name = name;
        this.image = image;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "PNG", byteArrayOutputStream);
            } finally {
                byteArrayOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        bytes = byteArrayOutputStream.toByteArray();
    }

    public static byte[][] divideArray(byte[] source, int chunksize) {
        byte[][] ret = new byte[(int) Math.ceil(source.length / (double) chunksize)][chunksize];

        int start = 0;

        for (int i = 0; i < ret.length; i++) {
            ret[i] = Arrays.copyOfRange(source, start, start + chunksize);
            start += chunksize;
        }

        return ret;
    }

    @Override
    public void writePacket(PacketDataSerializer packetBuffer) {

    }

    @Override
    public void sendPacket(EntityPlayer entityPlayer) {

        if (bytes.length <= 20000) {
            PacketDataSerializer dataPacket = new PacketDataSerializer(Unpooled.buffer());
            dataPacket.a(name);
            dataPacket.writeByte(0);
            dataPacket.a(bytes);
            entityPlayer.playerConnection.sendPacket(new PacketPlayOutCustomPayload(getPacketType(), dataPacket));
            PacketDataSerializer finishPacket = new PacketDataSerializer(Unpooled.buffer());
            finishPacket.a(name);
            finishPacket.writeByte(1);
            entityPlayer.playerConnection.sendPacket(new PacketPlayOutCustomPayload(getPacketType(), finishPacket));
            return;
        }
        for (byte[] bytes1 : divideArray(bytes, bytes.length / 20000)) {
            PacketDataSerializer dataPacket = new PacketDataSerializer(Unpooled.buffer());
            dataPacket.a(name);
            dataPacket.writeByte(0);
            dataPacket.a(bytes1);
            entityPlayer.playerConnection.sendPacket(new PacketPlayOutCustomPayload(getPacketType(), dataPacket));
        }
        PacketDataSerializer finishPacket = new PacketDataSerializer(Unpooled.buffer());
        finishPacket.a(name);
        finishPacket.writeByte(1);
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutCustomPayload(getPacketType(), finishPacket));
    }

    @Override
    public String getPacketType() {
        return PacketTypes.RecvTexture.name();
    }
}
