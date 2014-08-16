package uk.co.thinkofdeath.prismarine.network.protocol.login;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.ILoginHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class SetInitialCompression implements Packet<ILoginHandlerClientbound> {

    private int threshold;

    public SetInitialCompression() {
    }

    public SetInitialCompression(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void read(MCByteBuf buf) {
        threshold = buf.readVarInt();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(threshold);
    }

    @Override
    public void handle(ILoginHandlerClientbound handler) {
        handler.handle(this);
    }
}
