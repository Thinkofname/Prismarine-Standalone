package uk.co.thinkofdeath.micromc.network;

import uk.co.thinkofdeath.micromc.MicroMC;
import uk.co.thinkofdeath.micromc.chat.TextComponent;
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
                handler.getChannel().pipeline().get(PacketCodec.class)
                        .setProtocol(Protocol.LOGIN);
                handler.setHandler(new LoginHandler());

                if (handshake.getProtocolVersion() < MicroMC.PROTOCOL_VERSION) {
                    handler.disconnect(new TextComponent("Client out of date. This server is using " + MicroMC.MINECRAFT_VERSION
                            + " (" + handshake.getProtocolVersion() + ":" + MicroMC.PROTOCOL_VERSION + ")"));
                    return;
                } else if (handshake.getProtocolVersion() > MicroMC.PROTOCOL_VERSION) {
                    handler.disconnect(new TextComponent("Server out of data. This server is using " + MicroMC.MINECRAFT_VERSION
                            + " (" + handshake.getProtocolVersion() + ":" + MicroMC.PROTOCOL_VERSION + ")"));
                    return;
                }
                break;
            default:
                throw new RuntimeException("Unknown state");
        }
    }

    @Override
    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
    }
}
