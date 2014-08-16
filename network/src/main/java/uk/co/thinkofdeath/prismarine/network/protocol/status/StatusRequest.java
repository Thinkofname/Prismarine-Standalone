package uk.co.thinkofdeath.prismarine.network.protocol.status;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IStatusHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class StatusRequest implements Packet<IStatusHandlerServerbound> {

    public StatusRequest() {
    }

    @Override
    public void read(MCByteBuf buf) {

    }

    @Override
    public void write(MCByteBuf buf) {

    }

    @Override
    public void handle(IStatusHandlerServerbound handler) {
        handler.handle(this);
    }


}
