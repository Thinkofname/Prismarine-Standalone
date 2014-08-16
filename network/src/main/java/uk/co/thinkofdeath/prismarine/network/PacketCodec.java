package uk.co.thinkofdeath.prismarine.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;
import uk.co.thinkofdeath.prismarine.network.protocol.Protocol;
import uk.co.thinkofdeath.prismarine.network.protocol.ProtocolDirection;

import java.util.List;

public class PacketCodec extends ByteToMessageCodec<Packet> {

    private Protocol protocol;

    public PacketCodec(Protocol protocol) {
        this.protocol = protocol;
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
        Packet packet = protocol.create(ProtocolDirection.SERVERBOUND, id);
        if (packet != null) {
            packet.read(buf);
            out.add(packet);
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
