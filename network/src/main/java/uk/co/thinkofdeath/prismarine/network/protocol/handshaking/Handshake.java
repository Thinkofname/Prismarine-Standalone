package uk.co.thinkofdeath.prismarine.network.protocol.handshaking;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IHandshakingHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;
import uk.co.thinkofdeath.prismarine.network.protocol.Protocol;

public class Handshake implements Packet<IHandshakingHandlerServerbound> {

    private int protocolVersion;
    private String address;
    private int port;
    private Protocol next;

    public Handshake() {
    }

    public Handshake(int protocolVersion, String address, int port, Protocol next) {
        this.protocolVersion = protocolVersion;
        this.address = address;
        this.port = port;
        this.next = next;
    }

    @Override
    public void read(MCByteBuf buf) {
        protocolVersion = buf.readVarInt();
        address = buf.readString(255);
        port = buf.readUnsignedShort();
        int n = buf.readVarInt();
        for (Protocol protocol : Protocol.values()) {
            if (protocol.getId() == n) {
                next = protocol;
                break;
            }
        }
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(protocolVersion);
        buf.writeString(address);
        buf.writeShort(port);
        buf.writeVarInt(next.getId());
    }

    @Override
    public void handle(IHandshakingHandlerServerbound handler) {
        handler.handle(this);
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public Protocol getNext() {
        return next;
    }
}
