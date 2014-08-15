package uk.co.thinkofdeath.prismarine.network.protocol.status;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.StatusHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class StatusPing implements Packet<StatusHandler> {

    private long time;

    @Override
    public void read(MCByteBuf buf) {
        time = buf.readLong();
    }

    @Override
    public void handle(StatusHandler handler) {
        handler.handle(this);
    }

    public long getTime() {
        return time;
    }
}
