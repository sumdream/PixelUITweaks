package com.github.timmyovo.pixeluitweaks.client.hook;


import com.github.timmyovo.pixeluitweaks.client.PixelUITweaks;
import com.github.timmyovo.pixeluitweaks.client.gui.container.CommonContainer;
import com.github.timmyovo.pixeluitweaks.client.texture.IDownloadTexture;
import com.github.timmyovo.pixeluitweaks.common.api.IComp;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ClientHook implements IComp<ClientHook> {

    public void notifyGuiReceive(FMLProxyPacket fmlProxyPacket) {
        CPacketCustomPayload cPacketCustomPayload = (CPacketCustomPayload) fmlProxyPacket.toC17Packet();
        String s = cPacketCustomPayload.getBufferData().readString(Short.MAX_VALUE);
        CommonContainer o = GuiFactory.fromString(s, CommonContainer.class);
    }

    public void notifyPictureReceive(FMLProxyPacket fmlProxyPacket) {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            CPacketCustomPayload cPacketCustomPayload = (CPacketCustomPayload) fmlProxyPacket.toC17Packet();
            PacketBuffer bufferData = cPacketCustomPayload.getBufferData();
            String name = bufferData.readString(32);
            System.out.println(name);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bufferData.readByteArray());
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

    @Override
    public ClientHook init() {
        return this;
    }

    public void notifyInventoryReceive(FMLProxyPacket packet) {
        CPacketCustomPayload cPacketCustomPayload = (CPacketCustomPayload) packet.toC17Packet();
        int windowId = cPacketCustomPayload.getBufferData().readInt();
        String readUTF8String = ByteBufUtils.readUTF8String(cPacketCustomPayload.getBufferData());
        EntityPlayer playerSP = FMLClientHandler.instance().getClient().player;
        System.out.println(windowId);
        System.out.println(readUTF8String);
        playerSP.openGui("pixeluiutility", 0, null, 0, 0, 0);
        playerSP.openContainer.windowId = (int) windowId;
    }
}
