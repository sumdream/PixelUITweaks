package com.github.timmyovo.pixeluitweaks.server.inventory.network.utils;

import io.netty.buffer.ByteBuf;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.PacketDataSerializer;
import org.apache.commons.lang3.Validate;

import javax.annotation.Nullable;
import java.nio.charset.StandardCharsets;

public class ByteBufUtils {

    /**
     * Read an {@link NBTTagCompound} from the byte buffer. It uses the minecraft encoding.
     *
     * @param from The buffer to read from
     * @return The read tag
     */
    @Nullable
    public static NBTTagCompound readTag(ByteBuf from) {
        PacketDataSerializer pb = new PacketDataSerializer(from);
        //readCompoundTag
        return pb.j();
    }

    /**
     * Write an {@link NBTTagCompound} to the byte buffer. It uses the minecraft encoding.
     *
     * @param to  The buffer to write to
     * @param tag The tag to write
     */
    public static void writeTag(ByteBuf to, NBTTagCompound tag) {
        PacketDataSerializer pb = new PacketDataSerializer(to);
        //writeCompoundTag
        pb.a(tag);
    }


    /**
     * The number of bytes to write the supplied int using the 7 bit varint encoding.
     *
     * @param toCount The number to analyse
     * @return The number of bytes it will take to write it (maximum of 5)
     */
    public static int varIntByteCount(int toCount) {
        return (toCount & 0xFFFFFF80) == 0 ? 1 : ((toCount & 0xFFFFC000) == 0 ? 2 : ((toCount & 0xFFE00000) == 0 ? 3 : ((toCount & 0xF0000000) == 0 ? 4 : 5)));
    }

    /**
     * Read a varint from the supplied buffer.
     *
     * @param buf     The buffer to read from
     * @param maxSize The maximum length of bytes to read
     * @return The integer
     */
    public static int readVarInt(ByteBuf buf, int maxSize) {
        Validate.isTrue(maxSize < 6 && maxSize > 0, "Varint length is between 1 and 5, not %d", maxSize);
        int i = 0;
        int j = 0;
        byte b0;

        do {
            b0 = buf.readByte();
            i |= (b0 & 127) << j++ * 7;

            if (j > maxSize) {
                throw new RuntimeException("VarInt too big");
            }
        }
        while ((b0 & 128) == 128);

        return i;
    }

    /**
     * An extended length short. Used by custom payload packets to extend size.
     *
     * @param buf
     * @return
     */
    public static int readVarShort(ByteBuf buf) {
        int low = buf.readUnsignedShort();
        int high = 0;
        if ((low & 0x8000) != 0) {
            low = low & 0x7FFF;
            high = buf.readUnsignedByte();
        }
        return ((high & 0xFF) << 15) | low;
    }

    public static void writeVarShort(ByteBuf buf, int toWrite) {
        int low = toWrite & 0x7FFF;
        int high = (toWrite & 0x7F8000) >> 15;
        if (high != 0) {
            low = low | 0x8000;
        }
        buf.writeShort(low);
        if (high != 0) {
            buf.writeByte(high);
        }
    }

    /**
     * Write an integer to the buffer using variable length encoding. The maxSize constrains
     * how many bytes (and therefore the maximum number) that will be written.
     *
     * @param to      The buffer to write to
     * @param toWrite The integer to write
     * @param maxSize The maximum number of bytes to use
     */
    public static void writeVarInt(ByteBuf to, int toWrite, int maxSize) {
        Validate.isTrue(varIntByteCount(toWrite) <= maxSize, "Integer is too big for %d bytes", maxSize);
        while ((toWrite & -128) != 0) {
            to.writeByte(toWrite & 127 | 128);
            toWrite >>>= 7;
        }

        to.writeByte(toWrite);
    }

    /**
     * Read a UTF8 string from the byte buffer.
     * It is encoded as <varint length>[<UTF8 char bytes>]
     *
     * @param from The buffer to read from
     * @return The string
     */
    public static String readUTF8String(ByteBuf from) {
        int len = readVarInt(from, 2);
        String str = from.toString(from.readerIndex(), len, StandardCharsets.UTF_8);
        from.readerIndex(from.readerIndex() + len);
        return str;
    }

    /**
     * Write a String with UTF8 byte encoding to the buffer.
     * It is encoded as <varint length>[<UTF8 char bytes>]
     *
     * @param to     the buffer to write to
     * @param string The string to write
     */
    public static void writeUTF8String(ByteBuf to, String string) {
        byte[] utf8Bytes = string.getBytes(StandardCharsets.UTF_8);
        Validate.isTrue(varIntByteCount(utf8Bytes.length) < 3, "The string is too long for this encoding.");
        writeVarInt(to, utf8Bytes.length, 2);
        to.writeBytes(utf8Bytes);
    }

    public static byte[] readByteBufArray(ByteBuf byteBuf) {
        byte[] array = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(array);
        return array;
    }

    public static String getContentDump(ByteBuf buffer) {
        int currentLength = buffer.readableBytes();
        StringBuffer returnString = new StringBuffer((currentLength * 3) + // The
                // hex
                (currentLength) + // The ascii
                (currentLength / 4) + // The tabs/\n's
                30); // The text

        // returnString.append("Buffer contents:\n");
        int i, j; // Loop variables
        for (i = 0; i < currentLength; i++) {
            if ((i != 0) && (i % 16 == 0)) {
                // If it's a multiple of 16 and i isn't null, show the ascii
                returnString.append('\t');
                for (j = i - 16; j < i; j++) {
                    if (buffer.getByte(j) < 0x20 || buffer.getByte(j) > 0x7F)
                        returnString.append('.');
                    else
                        returnString.append((char) buffer.getByte(j));
                }
                // Add a linefeed after the string
                returnString.append("\n");
            }

            returnString.append(Integer.toString((buffer.getByte(i) & 0xF0) >> 4, 16) + Integer.toString((buffer.getByte(i) & 0x0F) >> 0, 16));
            returnString.append(' ');
        }

        // Add padding spaces if it's not a multiple of 16
        if (i != 0 && i % 16 != 0) {
            for (j = 0; j < ((16 - (i % 16)) * 3); j++) {
                returnString.append(' ');
            }
        }
        // Add the tab for alignment
        returnString.append('\t');

        // Add final characters at right, after padding

        // If it was at the end of a line, print out the full line
        if (i > 0 && (i % 16) == 0) {
            j = i - 16;
        } else {
            j = (i - (i % 16));
        }

        for (; i >= 0 && j < i; j++) {
            if (buffer.getByte(j) < 0x20 || buffer.getByte(j) > 0x7F)
                returnString.append('.');
            else
                returnString.append((char) buffer.getByte(j));
        }

        // Finally, tidy it all up with a newline
        returnString.append('\n');
        returnString.append("Length: " + currentLength);

        return returnString.toString();

    }
}