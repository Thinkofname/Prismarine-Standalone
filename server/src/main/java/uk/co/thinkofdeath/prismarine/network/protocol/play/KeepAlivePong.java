package uk.co.thinkofdeath.prismarine.network.protocol.play;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.PlayHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class KeepAlivePong implements Packet<PlayHandler> {

    private int id;

    @Override
    public void read(MCByteBuf buf) {
        id = buf.readVarInt();
    }

    @Override
    public void handle(PlayHandler handler) {
        handler.handle(this);
    }

    public int getId() {
        return id;
    }
}
