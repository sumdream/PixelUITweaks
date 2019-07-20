package com.github.timmyovo.pixeluitweaks.server.inventory.network;

import com.github.timmyovo.pixeluitweaks.server.inventory.network.utils.ByteBufUtils;
import io.netty.buffer.ByteBuf;

public class OpenGui {
    int windowId;
    String modId;
    int modGuiId;
    int x;
    int y;
    int z;

    public OpenGui() {
    }

    public OpenGui(int windowId, String modId, int modGuiId, int x, int y, int z) {
        this.windowId = windowId;
        this.modId = modId;
        this.modGuiId = modGuiId;
        this.x = x;
        this.y = y;
        this.z = z;
    }


    public void toBytes(ByteBuf buf) {
        buf.writeInt(windowId);
        ByteBufUtils.writeUTF8String(buf, modId);
        buf.writeInt(modGuiId);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
    }


    public void fromBytes(ByteBuf buf) {
        windowId = buf.readInt();
        modId = ByteBufUtils.readUTF8String(buf);
        modGuiId = buf.readInt();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
    }
}