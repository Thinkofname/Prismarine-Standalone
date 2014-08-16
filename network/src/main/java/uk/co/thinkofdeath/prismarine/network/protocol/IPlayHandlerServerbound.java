package uk.co.thinkofdeath.prismarine.network.protocol;

import uk.co.thinkofdeath.prismarine.network.protocol.play.KeepAlivePong;

public interface IPlayHandlerServerbound extends PacketHandler {
    void handle(KeepAlivePong keepAlivePong);
}
