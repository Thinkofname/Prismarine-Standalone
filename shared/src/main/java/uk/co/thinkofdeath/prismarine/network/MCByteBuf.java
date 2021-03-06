/*
 * Copyright 2014 Matthew Collins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.thinkofdeath.prismarine.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufProcessor;
import io.netty.handler.codec.DecoderException;
import uk.co.thinkofdeath.prismarine.Prismarine;
import uk.co.thinkofdeath.prismarine.chat.ChatSerializer;
import uk.co.thinkofdeath.prismarine.chat.Component;
import uk.co.thinkofdeath.prismarine.game.Position;
import uk.co.thinkofdeath.prismarine.item.ItemStack;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * An extension to netty's ByteBuf to support
 * the extra types used by Minecraft
 */
public class MCByteBuf extends ByteBuf {

    private final ByteBuf buf;

    /**
     * Wraps the provided ByteBuf
     *
     * @param buf
     *         the buffer to wrap
     */
    public MCByteBuf(ByteBuf buf) {
        this.buf = buf;
    }

    /**
     * Reads a UUID from the ByteBuf
     *
     * @return the read UUID
     */
    public UUID readUUID() {
        return new UUID(readLong(), readLong());
    }

    /**
     * Writes a UUID to the ByteBUf
     *
     * @param uuid
     *         the UUId to write
     */
    public void writeUUID(UUID uuid) {
        writeLong(uuid.getMostSignificantBits());
        writeLong(uuid.getLeastSignificantBits());
    }

    /**
     * Reads a position from the ByteBuf
     *
     * @return the read position
     */
    public Position readPosition() {
        return Position.fromLong(readLong());
    }

    /**
     * Writes a position to the ByteBuf
     *
     * @param position
     *         the position to write
     */
    public void writePosition(Position position) {
        writeLong(position.toLong());
    }

    /**
     * Reads an ItemStack from the ByteBuf
     *
     * @return the read item stack or null
     */
    public ItemStack readItemStack() {
        int id = readShort();
        if (id == -1) {
            return null;
        }
        int count = readByte();
        int damage = readShort();

        readNBTCompound(); // TODO

        return new ItemStack(
                Prismarine.getInstance().getItemRegistry().get(id),
                count,
                damage
        );
    }

    /**
     * Writes an ItemStack to the ByteBuf
     *
     * @param itemStack
     *         the item stack to write
     */
    public void writeItemStack(ItemStack itemStack) {
        if (itemStack == null) {
            writeShort(-1);
            return;
        }
        writeShort(Prismarine.getInstance().getItemRegistry().getId(itemStack.getItem()));
        writeByte(itemStack.getCount());
        writeShort(itemStack.getDamage());

        writeNBTCompound(); // TODO
    }

    /**
     * Reads an NBT Compound from the ByteBuf
     * (NYI)
     */
    public void readNBTCompound() {
        int length = readShort(); // NYI
        if (length != -1) {
            throw new UnsupportedOperationException();
        }
    }

    /**
     * Writes an NBT Compound to the ByteBuf
     * (NYI)
     */
    public void writeNBTCompound() {
        writeShort(-1);
        // NYI
    }

    /**
     * Reads a chat component from the ByteBuf
     *
     * @return the read chat component
     */
    public Component readChat() {
        return ChatSerializer.fromString(readString(Short.MAX_VALUE));
    }

    /**
     * Writes a chat component to the ByteBuf
     *
     * @param component
     *         the component to write
     */
    public void writeChat(Component component) {
        writeString(ChatSerializer.toString(component));
    }

    /**
     * Reads a length-prefixed byte array from the ByteBuf, limiting
     * its size.
     *
     * @param limit
     *         the size limit
     * @return the read byte array
     */
    public byte[] readByteArray(int limit) {
        int size = readVarInt();
        if (size > limit) {
            throw new DecoderException("Byte array too large (" + size + " > " + limit + ")");
        }
        byte[] data = new byte[size];
        readBytes(data);
        return data;
    }

    /**
     * Writes a length-prefixed byte array tto the ByteBuf.
     *
     * @param data
     *         the array to write
     */
    public void writeByteArray(byte[] data) {
        writeVarInt(data.length);
        writeBytes(data);
    }

    /**
     * Reads a length prefixed, utf-8 string from the ByteBuf, limiting its
     * size.
     *
     * @param limit
     *         the size limit
     * @return the read string
     */
    public String readString(int limit) {
        int length = readVarInt();
        if (length > limit * 4) {
            throw new DecoderException("String too long (" + length + " > " + limit + " * 4)");
        }
        String str = readBytes(length).toString(StandardCharsets.UTF_8);
        if (str.length() > limit) {
            throw new DecoderException("String too long (" + str.length() + " > " + limit + ")");
        }
        return str;
    }

    /**
     * Writes a length prefixed, utf-8 string to the ByteBuf
     *
     * @param val
     *         the string to write
     */
    public void writeString(String val) {
        byte[] bytes = val.getBytes(StandardCharsets.UTF_8);
        writeVarInt(bytes.length);
        writeBytes(bytes);
    }

    /**
     * Reads a varint from the ByteBuf
     *
     * @return the read varint
     */
    public int readVarInt() {
        int val = 0;
        int bytes = 0;
        while (true) {
            int b = readByte();
            val |= (b & 0b01111111) << (bytes++ * 7);
            if (bytes > 5) {
                throw new DecoderException("VarInt too big");
            }

            // If the 8th bit is set then the varint continues
            if ((b & 0x80) == 0) {
                break;
            }
        }
        return val;
    }

    /**
     * Reads a varint from the ByteBuf
     *
     * @param val
     *         the int to encode and write
     */
    public void writeVarInt(int val) {
        while (true) {
            if ((val & ~0b01111111) == 0) {
                writeByte(val);
                return;
            }

            writeByte((val & 0x7F) | 0x80);
            val >>>= 7;
        }
    }

    @Override
    public int capacity() {
        return buf.capacity();
    }

    @Override
    public ByteBuf capacity(int newCapacity) {
        return buf.capacity(newCapacity);
    }

    @Override
    public int maxCapacity() {
        return buf.maxCapacity();
    }

    @Override
    public ByteBufAllocator alloc() {
        return buf.alloc();
    }

    @Override
    public ByteOrder order() {
        return buf.order();
    }

    @Override
    public ByteBuf order(ByteOrder endianness) {
        return buf.order(endianness);
    }

    @Override
    public ByteBuf unwrap() {
        return buf.unwrap();
    }

    @Override
    public boolean isDirect() {
        return buf.isDirect();
    }

    @Override
    public int readerIndex() {
        return buf.readerIndex();
    }

    @Override
    public ByteBuf readerIndex(int readerIndex) {
        return buf.readerIndex(readerIndex);
    }

    @Override
    public int writerIndex() {
        return buf.writerIndex();
    }

    @Override
    public ByteBuf writerIndex(int writerIndex) {
        return buf.writerIndex(writerIndex);
    }

    @Override
    public ByteBuf setIndex(int readerIndex, int writerIndex) {
        return buf.setIndex(readerIndex, writerIndex);
    }

    @Override
    public int readableBytes() {
        return buf.readableBytes();
    }

    @Override
    public int writableBytes() {
        return buf.writableBytes();
    }

    @Override
    public int maxWritableBytes() {
        return buf.maxWritableBytes();
    }

    @Override
    public boolean isReadable() {
        return buf.isReadable();
    }

    @Override
    public boolean isReadable(int size) {
        return buf.isReadable(size);
    }

    @Override
    public boolean isWritable() {
        return buf.isWritable();
    }

    @Override
    public boolean isWritable(int size) {
        return buf.isWritable(size);
    }

    @Override
    public ByteBuf clear() {
        return buf.clear();
    }

    @Override
    public ByteBuf markReaderIndex() {
        return buf.markReaderIndex();
    }

    @Override
    public ByteBuf resetReaderIndex() {
        return buf.resetReaderIndex();
    }

    @Override
    public ByteBuf markWriterIndex() {
        return buf.markWriterIndex();
    }

    @Override
    public ByteBuf resetWriterIndex() {
        return buf.resetWriterIndex();
    }

    @Override
    public ByteBuf discardReadBytes() {
        return buf.discardReadBytes();
    }

    @Override
    public ByteBuf discardSomeReadBytes() {
        return buf.discardSomeReadBytes();
    }

    @Override
    public ByteBuf ensureWritable(int minWritableBytes) {
        return buf.ensureWritable(minWritableBytes);
    }

    @Override
    public int ensureWritable(int minWritableBytes, boolean force) {
        return buf.ensureWritable(minWritableBytes, force);
    }

    @Override
    public boolean getBoolean(int index) {
        return buf.getBoolean(index);
    }

    @Override
    public byte getByte(int index) {
        return buf.getByte(index);
    }

    @Override
    public short getUnsignedByte(int index) {
        return buf.getUnsignedByte(index);
    }

    @Override
    public short getShort(int index) {
        return buf.getShort(index);
    }

    @Override
    public int getUnsignedShort(int index) {
        return buf.getUnsignedShort(index);
    }

    @Override
    public int getMedium(int index) {
        return buf.getMedium(index);
    }

    @Override
    public int getUnsignedMedium(int index) {
        return buf.getUnsignedMedium(index);
    }

    @Override
    public int getInt(int index) {
        return buf.getInt(index);
    }

    @Override
    public long getUnsignedInt(int index) {
        return buf.getUnsignedInt(index);
    }

    @Override
    public long getLong(int index) {
        return buf.getLong(index);
    }

    @Override
    public char getChar(int index) {
        return buf.getChar(index);
    }

    @Override
    public float getFloat(int index) {
        return buf.getFloat(index);
    }

    @Override
    public double getDouble(int index) {
        return buf.getDouble(index);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst) {
        return buf.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int length) {
        return buf.getBytes(index, dst, length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
        return buf.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst) {
        return buf.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
        return buf.getBytes(index, dst, dstIndex, length);
    }

    @Override
    public ByteBuf getBytes(int index, ByteBuffer dst) {
        return buf.getBytes(index, dst);
    }

    @Override
    public ByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
        return buf.getBytes(index, out, length);
    }

    @Override
    public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
        return buf.getBytes(index, out, length);
    }

    @Override
    public ByteBuf setBoolean(int index, boolean value) {
        return buf.setBoolean(index, value);
    }

    @Override
    public ByteBuf setByte(int index, int value) {
        return buf.setByte(index, value);
    }

    @Override
    public ByteBuf setShort(int index, int value) {
        return buf.setShort(index, value);
    }

    @Override
    public ByteBuf setMedium(int index, int value) {
        return buf.setMedium(index, value);
    }

    @Override
    public ByteBuf setInt(int index, int value) {
        return buf.setInt(index, value);
    }

    @Override
    public ByteBuf setLong(int index, long value) {
        return buf.setLong(index, value);
    }

    @Override
    public ByteBuf setChar(int index, int value) {
        return buf.setChar(index, value);
    }

    @Override
    public ByteBuf setFloat(int index, float value) {
        return buf.setFloat(index, value);
    }

    @Override
    public ByteBuf setDouble(int index, double value) {
        return buf.setDouble(index, value);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src) {
        return buf.setBytes(index, src);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int length) {
        return buf.setBytes(index, src, length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
        return buf.setBytes(index, src, srcIndex, length);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src) {
        return buf.setBytes(index, src);
    }

    @Override
    public ByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
        return buf.setBytes(index, src, srcIndex, length);
    }

    @Override
    public ByteBuf setBytes(int index, ByteBuffer src) {
        return buf.setBytes(index, src);
    }

    @Override
    public int setBytes(int index, InputStream in, int length) throws IOException {
        return buf.setBytes(index, in, length);
    }

    @Override
    public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
        return buf.setBytes(index, in, length);
    }

    @Override
    public ByteBuf setZero(int index, int length) {
        return buf.setZero(index, length);
    }

    @Override
    public boolean readBoolean() {
        return buf.readBoolean();
    }

    @Override
    public byte readByte() {
        return buf.readByte();
    }

    @Override
    public short readUnsignedByte() {
        return buf.readUnsignedByte();
    }

    @Override
    public short readShort() {
        return buf.readShort();
    }

    @Override
    public int readUnsignedShort() {
        return buf.readUnsignedShort();
    }

    @Override
    public int readMedium() {
        return buf.readMedium();
    }

    @Override
    public int readUnsignedMedium() {
        return buf.readUnsignedMedium();
    }

    @Override
    public int readInt() {
        return buf.readInt();
    }

    @Override
    public long readUnsignedInt() {
        return buf.readUnsignedInt();
    }

    @Override
    public long readLong() {
        return buf.readLong();
    }

    @Override
    public char readChar() {
        return buf.readChar();
    }

    @Override
    public float readFloat() {
        return buf.readFloat();
    }

    @Override
    public double readDouble() {
        return buf.readDouble();
    }

    @Override
    public ByteBuf readBytes(int length) {
        return buf.readBytes(length);
    }

    @Override
    public ByteBuf readSlice(int length) {
        return buf.readSlice(length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst) {
        return buf.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int length) {
        return buf.readBytes(dst, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
        return buf.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuf readBytes(byte[] dst) {
        return buf.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(byte[] dst, int dstIndex, int length) {
        return buf.readBytes(dst, dstIndex, length);
    }

    @Override
    public ByteBuf readBytes(ByteBuffer dst) {
        return buf.readBytes(dst);
    }

    @Override
    public ByteBuf readBytes(OutputStream out, int length) throws IOException {
        return buf.readBytes(out, length);
    }

    @Override
    public int readBytes(GatheringByteChannel out, int length) throws IOException {
        return buf.readBytes(out, length);
    }

    @Override
    public ByteBuf skipBytes(int length) {
        return buf.skipBytes(length);
    }

    @Override
    public ByteBuf writeBoolean(boolean value) {
        return buf.writeBoolean(value);
    }

    @Override
    public ByteBuf writeByte(int value) {
        return buf.writeByte(value);
    }

    @Override
    public ByteBuf writeShort(int value) {
        return buf.writeShort(value);
    }

    @Override
    public ByteBuf writeMedium(int value) {
        return buf.writeMedium(value);
    }

    @Override
    public ByteBuf writeInt(int value) {
        return buf.writeInt(value);
    }

    @Override
    public ByteBuf writeLong(long value) {
        return buf.writeLong(value);
    }

    @Override
    public ByteBuf writeChar(int value) {
        return buf.writeChar(value);
    }

    @Override
    public ByteBuf writeFloat(float value) {
        return buf.writeFloat(value);
    }

    @Override
    public ByteBuf writeDouble(double value) {
        return buf.writeDouble(value);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src) {
        return buf.writeBytes(src);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int length) {
        return buf.writeBytes(src, length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
        return buf.writeBytes(src, srcIndex, length);
    }

    @Override
    public ByteBuf writeBytes(byte[] src) {
        return buf.writeBytes(src);
    }

    @Override
    public ByteBuf writeBytes(byte[] src, int srcIndex, int length) {
        return buf.writeBytes(src, srcIndex, length);
    }

    @Override
    public ByteBuf writeBytes(ByteBuffer src) {
        return buf.writeBytes(src);
    }

    @Override
    public int writeBytes(InputStream in, int length) throws IOException {
        return buf.writeBytes(in, length);
    }

    @Override
    public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
        return buf.writeBytes(in, length);
    }

    @Override
    public ByteBuf writeZero(int length) {
        return buf.writeZero(length);
    }

    @Override
    public int indexOf(int fromIndex, int toIndex, byte value) {
        return buf.indexOf(fromIndex, toIndex, value);
    }

    @Override
    public int bytesBefore(byte value) {
        return buf.bytesBefore(value);
    }

    @Override
    public int bytesBefore(int length, byte value) {
        return buf.bytesBefore(length, value);
    }

    @Override
    public int bytesBefore(int index, int length, byte value) {
        return buf.bytesBefore(index, length, value);
    }

    @Override
    public int forEachByte(ByteBufProcessor processor) {
        return buf.forEachByte(processor);
    }

    @Override
    public int forEachByte(int index, int length, ByteBufProcessor processor) {
        return buf.forEachByte(index, length, processor);
    }

    @Override
    public int forEachByteDesc(ByteBufProcessor processor) {
        return buf.forEachByteDesc(processor);
    }

    @Override
    public int forEachByteDesc(int index, int length, ByteBufProcessor processor) {
        return buf.forEachByteDesc(index, length, processor);
    }

    @Override
    public ByteBuf copy() {
        return buf.copy();
    }

    @Override
    public ByteBuf copy(int index, int length) {
        return buf.copy(index, length);
    }

    @Override
    public ByteBuf slice() {
        return buf.slice();
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return buf.slice(index, length);
    }

    @Override
    public ByteBuf duplicate() {
        return buf.duplicate();
    }

    @Override
    public int nioBufferCount() {
        return buf.nioBufferCount();
    }

    @Override
    public ByteBuffer nioBuffer() {
        return buf.nioBuffer();
    }

    @Override
    public ByteBuffer nioBuffer(int index, int length) {
        return buf.nioBuffer(index, length);
    }

    @Override
    public ByteBuffer internalNioBuffer(int index, int length) {
        return buf.internalNioBuffer(index, length);
    }

    @Override
    public ByteBuffer[] nioBuffers() {
        return buf.nioBuffers();
    }

    @Override
    public ByteBuffer[] nioBuffers(int index, int length) {
        return buf.nioBuffers(index, length);
    }

    @Override
    public boolean hasArray() {
        return buf.hasArray();
    }

    @Override
    public byte[] array() {
        return buf.array();
    }

    @Override
    public int arrayOffset() {
        return buf.arrayOffset();
    }

    @Override
    public boolean hasMemoryAddress() {
        return buf.hasMemoryAddress();
    }

    @Override
    public long memoryAddress() {
        return buf.memoryAddress();
    }

    @Override
    public String toString(Charset charset) {
        return buf.toString(charset);
    }

    @Override
    public String toString(int index, int length, Charset charset) {
        return buf.toString(index, length, charset);
    }

    @Override
    public int hashCode() {
        return buf.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return buf.equals(obj);
    }

    @Override
    public int compareTo(ByteBuf buffer) {
        return buf.compareTo(buffer);
    }

    @Override
    public String toString() {
        return buf.toString();
    }

    @Override
    public ByteBuf retain(int increment) {
        return buf.retain(increment);
    }

    @Override
    public ByteBuf retain() {
        return buf.retain();
    }

    @Override
    public int refCnt() {
        return buf.refCnt();
    }

    @Override
    public boolean release() {
        return buf.release();
    }

    @Override
    public boolean release(int decrement) {
        return buf.release(decrement);
    }
}
