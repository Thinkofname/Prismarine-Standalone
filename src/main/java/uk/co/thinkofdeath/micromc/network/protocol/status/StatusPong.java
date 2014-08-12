package uk.co.thinkofdeath.micromc.network.protocol.status;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class StatusPong implements Packet<NullHandler> {

    private long time;

    public StatusPong(long time) {
        this.time = time;
    }

    @Override
    public void read(MCByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeLong(time);
    }

    @Override
    public void handle(NullHandler handler) {
        throw new UnsupportedOperationException();
    }
}
