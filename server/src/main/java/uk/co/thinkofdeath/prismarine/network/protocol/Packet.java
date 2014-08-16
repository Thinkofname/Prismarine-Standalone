package uk.co.thinkofdeath.prismarine.network.protocol;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.PacketHandler;
import uk.co.thinkofdeath.prismarine.util.Stringable;

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
