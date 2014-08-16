package uk.co.thinkofdeath.prismarine.network.protocol;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.util.Stringable;

public interface Packet<H extends PacketHandler> extends Stringable {

    void read(MCByteBuf buf);

    void write(MCByteBuf buf);

    void handle(H handler);
}
