package uk.co.thinkofdeath.micromc.network.protocol;

import uk.co.thinkofdeath.micromc.network.protocol.handshaking.Handshake;
import uk.co.thinkofdeath.micromc.network.protocol.login.*;
import uk.co.thinkofdeath.micromc.network.protocol.play.*;
import uk.co.thinkofdeath.micromc.network.protocol.status.StatusPing;
import uk.co.thinkofdeath.micromc.network.protocol.status.StatusPong;
import uk.co.thinkofdeath.micromc.network.protocol.status.StatusReponse;
import uk.co.thinkofdeath.micromc.network.protocol.status.StatusRequest;

import java.util.HashMap;
import java.util.Map;

public enum Protocol {
    HANDSHAKING(-1) {{
        addPacket(ProtocolDirection.SERVERBOUND, Handshake.class);
    }},
    PLAY(0) {{


        addPacket(ProtocolDirection.CLIENTBOUND, KeepAlivePing.class);
        addPacket(ProtocolDirection.CLIENTBOUND, JoinGame.class);
        addPacket(ProtocolDirection.CLIENTBOUND, ServerMessage.class);
        addPacket(ProtocolDirection.CLIENTBOUND, TimeUpdate.class);
        skip(ProtocolDirection.CLIENTBOUND, 4);
        addPacket(ProtocolDirection.CLIENTBOUND, PlayerTeleport.class);
    }},
    STATUS(1) {{
        addPacket(ProtocolDirection.SERVERBOUND, StatusRequest.class);
        addPacket(ProtocolDirection.SERVERBOUND, StatusPing.class);

        addPacket(ProtocolDirection.CLIENTBOUND, StatusReponse.class);
        addPacket(ProtocolDirection.CLIENTBOUND, StatusPong.class);
    }},
    LOGIN(2) {{
        addPacket(ProtocolDirection.SERVERBOUND, LoginStart.class);
        addPacket(ProtocolDirection.SERVERBOUND, EncryptionResponse.class);

        addPacket(ProtocolDirection.CLIENTBOUND, LoginDisconnect.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EncryptionRequest.class);
        addPacket(ProtocolDirection.CLIENTBOUND, LoginSuccess.class);
        addPacket(ProtocolDirection.CLIENTBOUND, SetInitialCompression.class);
    }};

    private final int id;
    private final Class<? extends Packet>[] packetsByIdClientbound = new Class[256];
    private int nextClientboundId = 0;
    private final Class<? extends Packet>[] packetsByIdServerbound = new Class[256];
    private int nextServerboundId = 0;
    private final Map<Class<? extends Packet>, Integer> packetsByClass = new HashMap<>();

    Protocol(int id) {
        this.id = id;
    }

    public Packet create(ProtocolDirection direction, int id) {
        Class<? extends Packet>[] packets = direction == ProtocolDirection.CLIENTBOUND
                ? packetsByIdClientbound : packetsByIdServerbound;
        if (id < 0 || id >= packets.length) {
            return null;
        }
        try {
            return packets[id] == null ? null : packets[id].newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPacketId(Packet packet) {
        return getPacketId(packet.getClass());
    }

    private int getPacketId(Class<? extends Packet> packetClass) {
        return packetsByClass.get(packetClass);
    }

    protected void addPacket(ProtocolDirection direction, Class<? extends Packet> packet) {
        int pid = direction == ProtocolDirection.CLIENTBOUND ? nextClientboundId++ : nextServerboundId++;
        packetsByClass.put(packet, pid);
        if (direction == ProtocolDirection.CLIENTBOUND) {
            packetsByIdClientbound[pid] = packet;
        } else {
            packetsByIdServerbound[pid] = packet;
        }
    }

    // Exists until all packets are implemented
    @Deprecated
    protected void skip(ProtocolDirection direction, int count) {
        if (direction == ProtocolDirection.CLIENTBOUND) {
            nextClientboundId += count;
        } else {
            nextServerboundId += count;
        }
    }

    public int getId() {
        return id;
    }
}
