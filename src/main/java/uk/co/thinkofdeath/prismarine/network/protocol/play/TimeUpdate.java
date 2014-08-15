package uk.co.thinkofdeath.prismarine.network.protocol.play;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.NullHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class TimeUpdate implements Packet<NullHandler> {

    private long worldAge;
    private long time;

    public TimeUpdate(long worldAge, long time) {
        this.worldAge = worldAge;
        this.time = time;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeLong(worldAge);
        buf.writeLong(time);
    }
}
