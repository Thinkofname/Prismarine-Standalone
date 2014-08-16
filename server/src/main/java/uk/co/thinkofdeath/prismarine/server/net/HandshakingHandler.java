package uk.co.thinkofdeath.prismarine.server.net;

import uk.co.thinkofdeath.prismarine.chat.TextComponent;
import uk.co.thinkofdeath.prismarine.network.Constants;
import uk.co.thinkofdeath.prismarine.network.NetworkHandler;
import uk.co.thinkofdeath.prismarine.network.PacketCodec;
import uk.co.thinkofdeath.prismarine.network.protocol.IHandshakingHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Protocol;
import uk.co.thinkofdeath.prismarine.network.protocol.handshaking.Handshake;

public class HandshakingHandler implements IHandshakingHandlerServerbound {

    private NetworkHandler handler;

    @Override
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

                if (handshake.getProtocolVersion() < Constants.PROTOCOL_VERSION) {
                    handler.disconnect(new TextComponent("Client out of date. This server is using " + Constants.MINECRAFT_VERSION
                            + " (" + handshake.getProtocolVersion() + ":" + Constants.PROTOCOL_VERSION + ")"));
                    return;
                } else if (handshake.getProtocolVersion() > Constants.PROTOCOL_VERSION) {
                    handler.disconnect(new TextComponent("Server out of data. This server is using " + Constants.MINECRAFT_VERSION
                            + " (" + handshake.getProtocolVersion() + ":" + Constants.PROTOCOL_VERSION + ")"));
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
