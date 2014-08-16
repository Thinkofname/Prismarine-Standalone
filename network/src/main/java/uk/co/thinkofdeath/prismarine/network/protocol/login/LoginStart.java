package uk.co.thinkofdeath.prismarine.network.protocol.login;

import uk.co.thinkofdeath.prismarine.network.LoginHandler;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class LoginStart implements Packet<LoginHandler> {

    private String username;

    @Override
    public void read(MCByteBuf buf) {
        username = buf.readString(16);
    }

    @Override
    public void handle(LoginHandler handler) {
        handler.handle(this);
    }

    public String getUsername() {
        return username;
    }
}
