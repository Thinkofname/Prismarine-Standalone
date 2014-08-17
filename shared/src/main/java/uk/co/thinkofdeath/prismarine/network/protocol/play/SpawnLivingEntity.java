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

package uk.co.thinkofdeath.prismarine.network.protocol.play;

import uk.co.thinkofdeath.prismarine.entity.metadata.EntityMetadata;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class SpawnLivingEntity implements Packet<IPlayHandlerClientbound> {

    private int entityId;
    private int type;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;
    private byte headYaw;
    private short velX;
    private short velY;
    private short velZ;
    private EntityMetadata metadata;

    public SpawnLivingEntity() {
    }

    public SpawnLivingEntity(int entityId, int type, int x, int y, int z,
                             byte yaw, byte pitch, byte headYaw,
                             short velX, short velY, short velZ, EntityMetadata metadata) {
        this.entityId = entityId;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.headYaw = headYaw;
        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
        this.metadata = metadata;
    }

    @Override
    public void read(MCByteBuf buf) {
        entityId = buf.readVarInt();
        type = buf.readUnsignedByte();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        yaw = buf.readByte();
        pitch = buf.readByte();
        headYaw = buf.readByte();
        velX = buf.readShort();
        velY = buf.readShort();
        velZ = buf.readShort();
        metadata = new EntityMetadata();
        metadata.read(buf);
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeByte(type);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(yaw);
        buf.writeByte(pitch);
        buf.writeByte(headYaw);
        buf.writeShort(velX);
        buf.writeShort(velY);
        buf.writeShort(velZ);
        metadata.write(buf);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
