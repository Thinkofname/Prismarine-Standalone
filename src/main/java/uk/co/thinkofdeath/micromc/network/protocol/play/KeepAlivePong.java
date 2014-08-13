package uk.co.thinkofdeath.micromc.network.protocol.play;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.PlayHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class KeepAlivePong implements Packet<PlayHandler> {

    private int id;

    @Override
    public void read(MCByteBuf buf) {

    }

    @Override
    public void handle(PlayHandler handler) {
        handler.handle(this);
    }

    public int getId() {
        return id;
    }
}
