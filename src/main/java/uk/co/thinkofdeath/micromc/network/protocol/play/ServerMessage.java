package uk.co.thinkofdeath.micromc.network.protocol.play;

import uk.co.thinkofdeath.micromc.chat.Component;
import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class ServerMessage implements Packet<NullHandler> {

    private Component message;
    private Type type;

    public ServerMessage(Component message, Type type) {
        this.message = message;
        this.type = type;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeChat(message);
        buf.writeByte(type.ordinal());
    }

    public static enum Type {
        CHAT,
        SYSTEM,
        NOTIFY
    }
}
