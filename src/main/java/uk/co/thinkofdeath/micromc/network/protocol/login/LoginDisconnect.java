package uk.co.thinkofdeath.micromc.network.protocol.login;

import uk.co.thinkofdeath.micromc.chat.Component;
import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

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
