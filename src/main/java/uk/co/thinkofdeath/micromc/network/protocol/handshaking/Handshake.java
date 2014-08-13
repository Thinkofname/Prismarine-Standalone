package uk.co.thinkofdeath.micromc.network.protocol.handshaking;

import uk.co.thinkofdeath.micromc.network.HandshakingHandler;
import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;
import uk.co.thinkofdeath.micromc.network.protocol.Protocol;

public class Handshake implements Packet<HandshakingHandler> {

    private int protocolVersion;
    private String address;
    private int port;
    private Protocol next;

    public Handshake() {
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
    public void handle(HandshakingHandler handler) {
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
