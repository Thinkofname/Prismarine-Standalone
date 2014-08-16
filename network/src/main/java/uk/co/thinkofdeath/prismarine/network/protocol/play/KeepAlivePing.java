package uk.co.thinkofdeath.prismarine.network.protocol.play;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class KeepAlivePing implements Packet<IPlayHandlerClientbound> {

    private int id;

    public KeepAlivePing() {
    }

    public KeepAlivePing(int id) {
        this.id = id;
    }

    @Override
    public void read(MCByteBuf buf) {
        id = buf.readVarInt();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(id);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
