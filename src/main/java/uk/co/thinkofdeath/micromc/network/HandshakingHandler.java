package uk.co.thinkofdeath.micromc.network;

import uk.co.thinkofdeath.micromc.network.protocol.Protocol;
import uk.co.thinkofdeath.micromc.network.protocol.handshaking.Handshake;

public class HandshakingHandler implements PacketHandler {

    private NetworkHandler handler;

    public void handle(Handshake handshake) {
        switch (handshake.getNext()) {
            case STATUS:
                handler.getChannel().pipeline().get(PacketCodec.class)
                        .setProtocol(Protocol.STATUS);
                handler.setHandler(new StatusHandler());
                break;
            case LOGIN:
                throw new UnsupportedOperationException("NYI");
            default:
                throw new RuntimeException("Unknown state");
        }
    }

    @Override
    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
    }
}
