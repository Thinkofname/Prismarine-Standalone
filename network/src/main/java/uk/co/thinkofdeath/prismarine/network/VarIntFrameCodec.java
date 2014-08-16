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
import io.netty.handler.codec.DecoderException;

import java.util.List;

public class VarIntFrameCodec extends ByteToMessageCodec<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        MCByteBuf buf = new MCByteBuf(out);
        buf.ensureWritable(sizeOf(msg.readableBytes()) + msg.readableBytes());
        buf.writeVarInt(msg.readableBytes());
        buf.writeBytes(msg);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();

        int val = 0;
        int bytes = 0;
        while (true) {
            if (!in.isReadable()) {
                in.resetReaderIndex();
                return;
            }

            int b = in.readByte();
            val |= (b & 0b01111111) << (bytes++ * 7);
            if (bytes >= 3) { // Smaller limit for packets
                throw new DecoderException("VarInt too big");
            }

            if ((b & 0x80) == 0) {
                break;
            }
        }

        if (!in.isReadable(val)) {
            in.resetReaderIndex();
            return;
        }
        out.add(in.readBytes(val));
    }

    private static int sizeOf(int i) {
        if ((i & ~0b1111111) == 0) return 1;
        if ((i & ~0b111111111111111) == 0) return 2;
        if ((i & ~0b11111111111111111111111) == 0) return 3;
        if ((i & ~0b1111111111111111111111111111111) == 0) return 4;
        return 5;
    }
}
