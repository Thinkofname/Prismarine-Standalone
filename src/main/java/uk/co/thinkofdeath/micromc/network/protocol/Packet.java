package uk.co.thinkofdeath.micromc.network.protocol;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.PacketHandler;

public interface Packet<H extends PacketHandler> {

    void read(MCByteBuf buf);

    void write(MCByteBuf buf);

    void handle(H handler);
}
