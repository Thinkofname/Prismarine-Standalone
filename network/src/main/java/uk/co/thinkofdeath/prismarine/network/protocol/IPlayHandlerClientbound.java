package uk.co.thinkofdeath.prismarine.network.protocol;

import uk.co.thinkofdeath.prismarine.network.protocol.play.*;

public interface IPlayHandlerClientbound extends PacketHandler {
    void handle(JoinGame joinGame);

    void handle(KeepAlivePing keepAlivePing);

    void handle(PlayerTeleport playerTeleport);

    void handle(ServerMessage serverMessage);

    void handle(TimeUpdate timeUpdate);
}
