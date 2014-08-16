package uk.co.thinkofdeath.prismarine.network.protocol.play;

import uk.co.thinkofdeath.prismarine.chat.Component;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class ServerMessage implements Packet<IPlayHandlerClientbound> {

    private Component message;
    private Type type;

    public ServerMessage() {
    }

    public ServerMessage(Component message, Type type) {
        this.message = message;
        this.type = type;
    }

    @Override
    public void read(MCByteBuf buf) {
        message = buf.readChat();
        type = Type.values()[buf.readByte()];
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeChat(message);
        buf.writeByte(type.ordinal());
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }

    public static enum Type {
        CHAT,
        SYSTEM,
        NOTIFY
    }
}
