package uk.co.thinkofdeath.prismarine.network.protocol;

import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusPing;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusRequest;

public interface IStatusHandlerServerbound extends PacketHandler {
    void handle(StatusPing statusPing);

    void handle(StatusRequest statusRequest);
}
