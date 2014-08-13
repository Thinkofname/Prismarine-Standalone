package uk.co.thinkofdeath.micromc.network.protocol.login;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class SetInitialCompression implements Packet<NullHandler> {

    private int threshold;

    public SetInitialCompression(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void read(MCByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(threshold);
    }

    @Override
    public void handle(NullHandler handler) {
        throw new UnsupportedOperationException();
    }
}
