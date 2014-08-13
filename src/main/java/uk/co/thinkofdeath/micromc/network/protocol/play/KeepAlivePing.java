package uk.co.thinkofdeath.micromc.network.protocol.play;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

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
