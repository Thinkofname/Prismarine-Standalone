package uk.co.thinkofdeath.micromc.network.protocol.play;

import com.google.gson.Gson;
import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class ServerMessage implements Packet<NullHandler> {

    private static final Gson gson = new Gson();
    private String message; // TODO: Chat format
    private Type type;

    public ServerMessage(String message, Type type) {
        this.message = message;
        this.type = type;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeString(message);
        buf.writeByte(type.ordinal());
    }

    public static enum Type {
        CHAT,
        SYSTEM,
        NOTIFY
    }
}
