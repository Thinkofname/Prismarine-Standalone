package uk.co.thinkofdeath.prismarine.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import io.netty.handler.codec.DecoderException;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;
import uk.co.thinkofdeath.prismarine.network.protocol.Protocol;
import uk.co.thinkofdeath.prismarine.network.protocol.ProtocolDirection;

import java.util.List;

public class PacketCodec extends ByteToMessageCodec<Packet> {

    private Protocol protocol;
    private final ProtocolDirection incomingPacketType;

    public PacketCodec(Protocol protocol, ProtocolDirection incomingPacketType) {
        this.protocol = protocol;
        this.incomingPacketType = incomingPacketType;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        MCByteBuf buf = new MCByteBuf(out);
        buf.writeVarInt(protocol.getPacketId(msg));
        msg.write(buf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        MCByteBuf buf = new MCByteBuf(in);
        int id = buf.readVarInt();
        Packet packet = protocol.create(incomingPacketType, id);
        if (packet != null) {
            packet.read(buf);
            out.add(packet);
            if (in.readableBytes() > 0) {
                throw new DecoderException("Failed to read all bytes from " + id);
            }
        } else {
            buf.skipBytes(buf.readableBytes());
        }
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Protocol getProtocol() {
        return protocol;
    }
}
