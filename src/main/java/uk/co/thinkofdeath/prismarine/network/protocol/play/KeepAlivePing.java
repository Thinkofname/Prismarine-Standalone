package uk.co.thinkofdeath.prismarine.network.protocol.play;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.NullHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class KeepAlivePing implements Packet<NullHandler> {

    private int id;

    public KeepAlivePing(int id) {
        this.id = id;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(id);
    }
}
