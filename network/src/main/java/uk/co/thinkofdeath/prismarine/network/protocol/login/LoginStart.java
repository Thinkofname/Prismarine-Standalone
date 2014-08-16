package uk.co.thinkofdeath.prismarine.network.protocol.login;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.ILoginHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class LoginStart implements Packet<ILoginHandlerServerbound> {

    private String username;

    public LoginStart() {
    }

    public LoginStart(String username) {
        this.username = username;
    }

    @Override
    public void read(MCByteBuf buf) {
        username = buf.readString(16);
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(username);
    }

    @Override
    public void handle(ILoginHandlerServerbound handler) {
        handler.handle(this);
    }

    public String getUsername() {
        return username;
    }
}
