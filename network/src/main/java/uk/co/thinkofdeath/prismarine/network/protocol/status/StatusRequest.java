package uk.co.thinkofdeath.prismarine.network.protocol.status;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.StatusHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class StatusRequest implements Packet<StatusHandler> {
    @Override
    public void read(MCByteBuf buf) {

    }

    @Override
    public void handle(StatusHandler handler) {
        handler.handle(this);
    }


}
