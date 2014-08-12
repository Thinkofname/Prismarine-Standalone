package uk.co.thinkofdeath.micromc.network.protocol.status;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.StatusHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class StatusRequest implements Packet<StatusHandler> {
    @Override
    public void read(MCByteBuf buf) {

    }

    @Override
    public void write(MCByteBuf buf) {

    }

    @Override
    public void handle(StatusHandler handler) {
        handler.handle(this);
    }


}
