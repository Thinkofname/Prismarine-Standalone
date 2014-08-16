package uk.co.thinkofdeath.prismarine.network.protocol.login;

import uk.co.thinkofdeath.prismarine.chat.Component;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.NullHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class LoginDisconnect implements Packet<NullHandler> {

    private Component reason;

    public LoginDisconnect(Component reason) {
        this.reason = reason;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeChat(reason);
    }
}
