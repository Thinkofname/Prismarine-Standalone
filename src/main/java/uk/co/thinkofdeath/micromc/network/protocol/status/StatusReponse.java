package uk.co.thinkofdeath.micromc.network.protocol.status;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class StatusReponse implements Packet<NullHandler> {

    private String response;

    public StatusReponse(String response) {
        this.response = response;
    }

    @Override
    public void read(MCByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(response);
    }

    @Override
    public void handle(NullHandler handler) {
        throw new UnsupportedOperationException();
    }
}
