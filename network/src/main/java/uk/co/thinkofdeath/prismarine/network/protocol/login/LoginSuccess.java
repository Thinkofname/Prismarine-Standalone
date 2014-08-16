package uk.co.thinkofdeath.prismarine.network.protocol.login;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.ILoginHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class LoginSuccess implements Packet<ILoginHandlerClientbound> {

    private String uuid;
    private String username;

    public LoginSuccess() {
    }

    public LoginSuccess(String uuid, String username) {
        this.uuid = uuid;
        this.username = username;
    }

    @Override
    public void read(MCByteBuf buf) {
        uuid = buf.readString(40);
        username = buf.readString(16);
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(uuid);
        buf.writeString(username);
    }

    @Override
    public void handle(ILoginHandlerClientbound handler) {
        handler.handle(this);
    }
}
