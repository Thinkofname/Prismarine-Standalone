package uk.co.thinkofdeath.prismarine.network.protocol.login;

import uk.co.thinkofdeath.prismarine.chat.Component;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.ILoginHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class LoginDisconnect implements Packet<ILoginHandlerClientbound> {

    private Component reason;

    public LoginDisconnect() {
    }

    public LoginDisconnect(Component reason) {
        this.reason = reason;
    }

    @Override
    public void read(MCByteBuf buf) {
        reason = buf.readChat();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeChat(reason);
    }

    @Override
    public void handle(ILoginHandlerClientbound handler) {
        handler.handle(this);
    }
}
