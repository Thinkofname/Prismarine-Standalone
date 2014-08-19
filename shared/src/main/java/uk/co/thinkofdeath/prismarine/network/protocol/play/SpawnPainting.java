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

import uk.co.thinkofdeath.prismarine.game.Position;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class SpawnPainting implements Packet<IPlayHandlerClientbound> {

    private int entityId;
    private String title;
    private Position position;
    private int direction;

    public SpawnPainting() {
    }

    public SpawnPainting(int entityId, String title, Position position, int direction) {
        this.entityId = entityId;
        this.title = title;
        this.position = position;
        this.direction = direction;
    }

    @Override
    public void read(MCByteBuf buf) {
        entityId = buf.readVarInt();
        title = buf.readString(13);
        position = buf.readPosition();
        direction = buf.readUnsignedByte();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeString(title);
        buf.writePosition(position);
        buf.writeByte(direction);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
