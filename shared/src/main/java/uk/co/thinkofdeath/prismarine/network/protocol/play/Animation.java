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

public class Animation implements Packet<IPlayHandlerClientbound> {

    private int entityId;
    private int animation;

    public Animation() {
    }

    public Animation(int entityId, int animation) {
        this.entityId = entityId;
        this.animation = animation;
    }

    @Override
    public void read(MCByteBuf buf) {
        entityId = buf.readVarInt();
        animation = buf.readUnsignedByte();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeByte(animation);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
