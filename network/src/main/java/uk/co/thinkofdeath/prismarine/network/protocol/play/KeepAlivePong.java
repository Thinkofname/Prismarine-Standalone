package uk.co.thinkofdeath.prismarine.network.protocol.play;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class KeepAlivePong implements Packet<IPlayHandlerServerbound> {

    private int id;

    public KeepAlivePong() {
    }

    public KeepAlivePong(int id) {
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
    public void handle(IPlayHandlerServerbound handler) {
        handler.handle(this);
    }

    public int getId() {
        return id;
    }
}
