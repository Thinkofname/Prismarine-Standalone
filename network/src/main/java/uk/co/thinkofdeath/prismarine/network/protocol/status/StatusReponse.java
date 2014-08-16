package uk.co.thinkofdeath.prismarine.network.protocol.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uk.co.thinkofdeath.prismarine.chat.ChatSerializer;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.ping.Ping;
import uk.co.thinkofdeath.prismarine.network.protocol.IStatusHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class StatusReponse implements Packet<IStatusHandlerClientbound> {

    private static final Gson gson = ChatSerializer.attach(new GsonBuilder())
            .create();
    private Ping response;

    public StatusReponse() {
    }

    public StatusReponse(Ping response) {
        this.response = response;
    }

    @Override
    public void read(MCByteBuf buf) {
        response = gson.fromJson(buf.readString(Short.MAX_VALUE), Ping.class);
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(gson.toJson(response));
    }

    @Override
    public void handle(IStatusHandlerClientbound handler) {
        handler.handle(this);
    }
}
