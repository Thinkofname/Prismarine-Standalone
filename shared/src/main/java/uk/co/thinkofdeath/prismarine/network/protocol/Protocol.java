/*
 * Copyright 2014 Matthew Collins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.thinkofdeath.prismarine.network.protocol;

import uk.co.thinkofdeath.prismarine.network.protocol.handshaking.Handshake;
import uk.co.thinkofdeath.prismarine.network.protocol.login.*;
import uk.co.thinkofdeath.prismarine.network.protocol.play.*;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusPing;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusPong;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusReponse;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusRequest;

import java.util.HashMap;
import java.util.Map;

public enum Protocol {
    HANDSHAKING(-1) {{
        addPacket(ProtocolDirection.SERVERBOUND, Handshake.class);
    }},
    PLAY(0) {{
        addPacket(ProtocolDirection.SERVERBOUND, KeepAlivePong.class);
        addPacket(ProtocolDirection.SERVERBOUND, ChatMessage.class);

        addPacket(ProtocolDirection.CLIENTBOUND, KeepAlivePing.class);
        addPacket(ProtocolDirection.CLIENTBOUND, JoinGame.class);
        addPacket(ProtocolDirection.CLIENTBOUND, ServerMessage.class);
        addPacket(ProtocolDirection.CLIENTBOUND, TimeUpdate.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityEquipment.class);
        addPacket(ProtocolDirection.CLIENTBOUND, SpawnPosition.class);
        addPacket(ProtocolDirection.CLIENTBOUND, UpdateHealth.class);
        addPacket(ProtocolDirection.CLIENTBOUND, Respawn.class);
        addPacket(ProtocolDirection.CLIENTBOUND, PlayerTeleport.class);
        addPacket(ProtocolDirection.CLIENTBOUND, SetHeldItem.class);
        addPacket(ProtocolDirection.CLIENTBOUND, UseBed.class);
        addPacket(ProtocolDirection.CLIENTBOUND, Animation.class);
        addPacket(ProtocolDirection.CLIENTBOUND, SpawnPlayer.class);
        addPacket(ProtocolDirection.CLIENTBOUND, CollectItem.class);
        addPacket(ProtocolDirection.CLIENTBOUND, SpawnObject.class);
        addPacket(ProtocolDirection.CLIENTBOUND, SpawnLivingEntity.class);
        addPacket(ProtocolDirection.CLIENTBOUND, SpawnPainting.class);
        addPacket(ProtocolDirection.CLIENTBOUND, SpawnExperienceOrb.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityVelocity.class);
        addPacket(ProtocolDirection.CLIENTBOUND, DestroyEntities.class);
        addPacket(ProtocolDirection.CLIENTBOUND, Entity.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityMove.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityLook.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityMoveLook.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityTeleport.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityHeadLook.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityStatus.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityAttach.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntitySetMetadata.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityEffect.class);
        addPacket(ProtocolDirection.CLIENTBOUND, EntityRemoveEffect.class);
        addPacket(ProtocolDirection.CLIENTBOUND, SetExperience.class);
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
    @SuppressWarnings("unchecked")
    private final Class<? extends Packet>[] packetsByIdClientbound = new Class[256];
    private int nextClientboundId = 0;
    @SuppressWarnings("unchecked")
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
        try {
            packet.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(packet.getSimpleName() + " is missing a zero arg constructor");
        }

        int pid = direction == ProtocolDirection.CLIENTBOUND ? nextClientboundId++ : nextServerboundId++;
        packetsByClass.put(packet, pid);
        if (direction == ProtocolDirection.CLIENTBOUND) {
            packetsByIdClientbound[pid] = packet;
        } else {
            packetsByIdServerbound[pid] = packet;
        }
    }

    public int getId() {
        return id;
    }
}
