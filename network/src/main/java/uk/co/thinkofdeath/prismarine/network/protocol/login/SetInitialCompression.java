package uk.co.thinkofdeath.prismarine.network.protocol.login;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.NullHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class SetInitialCompression implements Packet<NullHandler> {

    private int threshold;

    public SetInitialCompression(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(threshold);
    }
}
