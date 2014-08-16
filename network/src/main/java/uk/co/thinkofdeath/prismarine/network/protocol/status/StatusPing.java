package uk.co.thinkofdeath.prismarine.network.protocol.status;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IStatusHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class StatusPing implements Packet<IStatusHandlerServerbound> {

    private long time;

    public StatusPing() {
    }

    public StatusPing(long time) {
        this.time = time;
    }

    @Override
    public void read(MCByteBuf buf) {
        time = buf.readLong();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeLong(time);
    }

    @Override
    public void handle(IStatusHandlerServerbound handler) {
        handler.handle(this);
    }

    public long getTime() {
        return time;
    }
}
