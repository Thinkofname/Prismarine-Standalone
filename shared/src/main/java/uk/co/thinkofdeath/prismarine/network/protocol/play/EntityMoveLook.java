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

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class EntityMoveLook implements Packet<IPlayHandlerClientbound> {

    private int entityId;
    private int dx;
    private int dy;
    private int dz;
    private int yaw;
    private int pitch;
    private boolean onGround;

    public EntityMoveLook() {
    }

    public EntityMoveLook(int entityId, int dx, int dy, int dz, int yaw, int pitch, boolean onGround) {
        this.entityId = entityId;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
    }

    @Override
    public void read(MCByteBuf buf) {
        entityId = buf.readVarInt();
        dx = buf.readByte();
        dy = buf.readByte();
        dz = buf.readByte();
        yaw = buf.readByte();
        pitch = buf.readByte();
        onGround = buf.readBoolean();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeByte(dx);
        buf.writeByte(dy);
        buf.writeByte(dz);
        buf.writeByte(yaw);
        buf.writeByte(pitch);
        buf.writeBoolean(onGround);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
