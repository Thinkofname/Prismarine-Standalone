package uk.co.thinkofdeath.micromc.network.protocol.status;

import com.google.gson.Gson;
import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.ping.Ping;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class StatusReponse implements Packet<NullHandler> {

    private static final Gson gson = new Gson();
    private Ping response;

    public StatusReponse(Ping response) {
        this.response = response;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(gson.toJson(response));
    }
}
