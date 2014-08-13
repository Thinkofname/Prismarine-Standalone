package uk.co.thinkofdeath.micromc.network.protocol.play;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

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
