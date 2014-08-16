package uk.co.thinkofdeath.prismarine.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class CompressionCodec extends ByteToMessageCodec<ByteBuf> {

    private int threshold;

    private Inflater inflater = new Inflater();
    private Deflater deflater = new Deflater();
    private byte[] dataBuffer = new byte[8192];
    private byte[] compBuffer = new byte[8192];
    private byte[] decompBuffer = new byte[8192];

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf msg, ByteBuf out) throws Exception {
        MCByteBuf buf = new MCByteBuf(out);
        if (msg.readableBytes() < threshold) {
            buf.writeVarInt(0);
            buf.writeBytes(msg);
        } else {

            byte[] data;
            int offset = 0;
            int dataSize;
            if (!msg.isDirect()) {
                data = msg.array();
                offset = msg.arrayOffset();
                dataSize = msg.readableBytes();
                msg.skipBytes(msg.readableBytes());
            } else {
                dataSize = msg.readableBytes();
                if (dataBuffer.length < dataSize) {
                    dataBuffer = new byte[dataSize];
                }
                msg.readBytes(dataBuffer, 0, dataSize);
                data = dataBuffer;
            }

            buf.writeVarInt(dataSize);

            deflater.setInput(data, offset, dataSize);
            deflater.finish();
            while (!deflater.finished()) {
                int count = deflater.deflate(compBuffer);
                out.writeBytes(compBuffer, 0, count);
            }
            deflater.reset();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MCByteBuf buf = new MCByteBuf(in);
        int size = buf.readVarInt();
        if (size == 0) {
            out.add(buf.readBytes(buf.readableBytes()));
        } else {
            if (decompBuffer.length < in.readableBytes()) {
                decompBuffer = new byte[in.readableBytes()];
            }
            int count = in.readableBytes();
            in.readBytes(decompBuffer, 0, count);
            inflater.setInput(decompBuffer, 0, count);

            ByteBuf oBuf = ctx.alloc().heapBuffer(size);
            oBuf.writerIndex(inflater.inflate(oBuf.array(), oBuf.arrayOffset(), size));
            out.add(oBuf);
            inflater.reset();
        }
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }
}
