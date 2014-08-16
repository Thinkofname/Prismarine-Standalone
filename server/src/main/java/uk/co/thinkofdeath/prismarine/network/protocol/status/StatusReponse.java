package uk.co.thinkofdeath.prismarine.network.protocol.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.co.thinkofdeath.prismarine.chat.ChatSerializer;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.NullHandler;
import uk.co.thinkofdeath.prismarine.network.ping.Ping;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class StatusReponse implements Packet<NullHandler> {

    private static final Gson gson = ChatSerializer.attach(new GsonBuilder())
            .create();
    private Ping response;

    public StatusReponse(Ping response) {
        this.response = response;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(gson.toJson(response));
    }
}
