package uk.co.thinkofdeath.prismarine.network.protocol.status;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IStatusHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class StatusPong implements Packet<IStatusHandlerClientbound> {

    private long time;

    public StatusPong() {
    }

    public StatusPong(long time) {
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
    public void handle(IStatusHandlerClientbound handler) {
        handler.handle(this);
    }
}
