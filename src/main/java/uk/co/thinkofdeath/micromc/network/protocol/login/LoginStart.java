package uk.co.thinkofdeath.micromc.network.protocol.login;

import uk.co.thinkofdeath.micromc.network.LoginHandler;
import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class LoginStart implements Packet<LoginHandler> {

    private String username;

    @Override
    public void read(MCByteBuf buf) {
        username = buf.readString(16);
    }

    @Override
    public void write(MCByteBuf buf) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void handle(LoginHandler handler) {
        handler.handle(this);
    }

    public String getUsername() {
        return username;
    }
}
