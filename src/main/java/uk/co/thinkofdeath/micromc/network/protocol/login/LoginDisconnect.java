package uk.co.thinkofdeath.micromc.network.protocol.login;

import com.google.gson.Gson;
import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class LoginDisconnect implements Packet<NullHandler> {

    private static final Gson gson = new Gson();
    private String reason;

    public LoginDisconnect(String reason) {
        this.reason = reason;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(gson.toJson(reason)); // TODO: Chat formatting
    }
}
