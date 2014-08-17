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
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;
import uk.co.thinkofdeath.prismarine.network.protocol.Protocol;
import uk.co.thinkofdeath.prismarine.network.protocol.ProtocolDirection;

import java.util.List;

/**
 * Reads and writes Minecraft packets. Each packet in the Minecraft
 * protocol as a varint packet id at the front, this codec handles
 * selecting the right packet based on the id as well as prepending
 * the id to outgoing packets
 */
public class PacketCodec extends ByteToMessageCodec<Packet> {

    private Protocol protocol;
    private final ProtocolDirection incomingPacketType;

    /**
     * Creates a PacketCodec which handles the set sub-protocol
     * (which may be changed later). The incomingPacketType is used
     * to know whether this is a client or a server.
     *
     * @param protocol
     *         the initial sub-protocol
     * @param incomingPacketType
     *         the direction of incoming packets
     */
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
            // TODO: We need to handle all packets
            buf.skipBytes(buf.readableBytes());
        }
    }

    /**
     * Sets the sub-protocol that this packet codec handles
     *
     * @param protocol
     *         the sub-protocol
     */
    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    /**
     * Returns the current protocol being used by this
     * packet codec
     *
     * @return the sub-protocol
     */
    public Protocol getProtocol() {
        return protocol;
    }
}
