package uk.co.thinkofdeath.prismarine.network.protocol;

import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusPong;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusReponse;

public interface IStatusHandlerClientbound extends PacketHandler {
    void handle(StatusPong statusPong);

    void handle(StatusReponse statusReponse);
}
