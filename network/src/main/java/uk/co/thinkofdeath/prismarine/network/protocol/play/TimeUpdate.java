package uk.co.thinkofdeath.prismarine.network.protocol.play;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class TimeUpdate implements Packet<IPlayHandlerClientbound> {

    private long worldAge;
    private long time;

    public TimeUpdate() {
    }

    public TimeUpdate(long worldAge, long time) {
        this.worldAge = worldAge;
        this.time = time;
    }

    @Override
    public void read(MCByteBuf buf) {
        worldAge = buf.readLong();
        time = buf.readLong();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeLong(worldAge);
        buf.writeLong(time);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
