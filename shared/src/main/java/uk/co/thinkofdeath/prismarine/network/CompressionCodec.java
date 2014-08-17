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
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * The compression codec will compress/decompress packets that fall within
 * the set threshold
 */
public class CompressionCodec extends ByteToMessageCodec<ByteBuf> {

    private int threshold;
    private ThreadLocal<CompressionInfo> info = new ThreadLocal<CompressionInfo>() {

        @Override
        protected CompressionInfo initialValue() {
            return new CompressionInfo();
        }
    };

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        MCByteBuf buf = new MCByteBuf(out);
        if (msg.readableBytes() < threshold) {
            buf.writeVarInt(0);
            buf.writeBytes(msg);
        } else {
            CompressionInfo ci = info.get();

            byte[] data;
            int offset = 0;
            int dataSize;
            // Handle both direct and heap buffers
            // heap buffers are faster in this case as they
            // do not require a copy
            if (!msg.isDirect()) {
                data = msg.array();
                offset = msg.arrayOffset();
                dataSize = msg.readableBytes();
                msg.skipBytes(msg.readableBytes());
            } else {
                dataSize = msg.readableBytes();
                if (ci.dataBuffer.length < dataSize) {
                    ci.dataBuffer = new byte[dataSize];
                }
                msg.readBytes(ci.dataBuffer, 0, dataSize);
                data = ci.dataBuffer;
            }

            buf.writeVarInt(dataSize);

            ci.deflater.setInput(data, offset, dataSize);
            ci.deflater.finish();
            while (!ci.deflater.finished()) {
                int count = ci.deflater.deflate(ci.compBuffer);
                out.writeBytes(ci.compBuffer, 0, count);
            }
            ci.deflater.reset();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MCByteBuf buf = new MCByteBuf(in);
        int size = buf.readVarInt();
        if (size == 0) {
            out.add(buf.readBytes(buf.readableBytes()));
        } else {
            CompressionInfo ci = info.get();
            if (ci.decompBuffer.length < in.readableBytes()) {
                ci.decompBuffer = new byte[in.readableBytes()];
            }
            int count = in.readableBytes();
            in.readBytes(ci.decompBuffer, 0, count);
            ci.inflater.setInput(ci.decompBuffer, 0, count);

            // Use heap buffers so we can just access the internal array
            ByteBuf oBuf = ctx.alloc().heapBuffer(size);
            oBuf.writerIndex(ci.inflater.inflate(oBuf.array(), oBuf.arrayOffset(), size));
            out.add(oBuf);
            ci.inflater.reset();
        }
    }

    /**
     * Gets the current threshold
     *
     * @return the threshold
     */
    public int getThreshold() {
        return threshold;
    }

    /**
     * Sets the new threshold for the codec
     *
     * @param threshold
     *         the new threshold
     */
    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    // Reusable buffers
    private static class CompressionInfo {

        public Inflater inflater = new Inflater();
        public Deflater deflater = new Deflater();
        public byte[] dataBuffer = new byte[8192];
        public byte[] compBuffer = new byte[8192];
        public byte[] decompBuffer = new byte[8192];
    }
}
