package uk.co.thinkofdeath.micromc.network.protocol;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.PacketHandler;
import uk.co.thinkofdeath.micromc.util.Stringable;

public interface Packet<H extends PacketHandler> extends Stringable {

    default void read(MCByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    default void write(MCByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    default void handle(H handler) {
        throw new UnsupportedOperationException();
    }
}
