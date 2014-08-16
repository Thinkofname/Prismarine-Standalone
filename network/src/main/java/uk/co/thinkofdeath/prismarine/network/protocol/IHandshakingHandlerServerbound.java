package uk.co.thinkofdeath.prismarine.network.protocol;

import uk.co.thinkofdeath.prismarine.network.protocol.handshaking.Handshake;

public interface IHandshakingHandlerServerbound extends PacketHandler {
    void handle(Handshake handshake);
}
