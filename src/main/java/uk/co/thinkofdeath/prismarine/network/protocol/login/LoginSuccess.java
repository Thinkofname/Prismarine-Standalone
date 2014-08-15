package uk.co.thinkofdeath.prismarine.network.protocol.login;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.NullHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class LoginSuccess implements Packet<NullHandler> {

    private String uuid;
    private String username;

    public LoginSuccess(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(uuid);
        buf.writeString(username);
    }
}
