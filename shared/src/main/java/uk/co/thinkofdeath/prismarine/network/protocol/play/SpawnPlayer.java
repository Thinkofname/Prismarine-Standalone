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

import uk.co.thinkofdeath.prismarine.Prismarine;
import uk.co.thinkofdeath.prismarine.entity.metadata.EntityMetadata;
import uk.co.thinkofdeath.prismarine.item.Item;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

import java.util.UUID;

public class SpawnPlayer implements Packet<IPlayHandlerClientbound> {

    private int entityId;
    private UUID uuid;
    private int x;
    private int y;
    private int z;
    private byte yaw;
    private byte pitch;
    private Item currentItem;
    private EntityMetadata metadata;

    public SpawnPlayer() {
    }

    public SpawnPlayer(int entityId, UUID uuid,
                       int x, int y, int z, byte yaw, byte pitch,
                       Item currentItem, EntityMetadata metadata) {
        this.entityId = entityId;
        this.uuid = uuid;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.currentItem = currentItem;
        this.metadata = metadata;
    }

    @Override
    public void read(MCByteBuf buf) {
        entityId = buf.readVarInt();
        uuid = buf.readUUID();
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        yaw = buf.readByte();
        pitch = buf.readByte();
        int item = buf.readUnsignedShort();
        if (item != 0) {
            currentItem = Prismarine.getInstance().getItemRegistry().get(item);
        }
        metadata = new EntityMetadata();
        metadata.read(buf);
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeUUID(uuid);
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeByte(yaw);
        buf.writeByte(pitch);
        if (currentItem == null) {
            buf.writeShort(0);
        } else {
            buf.writeShort(Prismarine.getInstance().getItemRegistry().getId(currentItem));
        }
        metadata.write(buf);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
