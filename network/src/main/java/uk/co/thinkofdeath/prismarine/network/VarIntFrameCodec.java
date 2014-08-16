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
