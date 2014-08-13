package uk.co.thinkofdeath.micromc.network.protocol.login;

import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

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
