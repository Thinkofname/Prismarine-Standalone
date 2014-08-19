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

public class EntityEffect implements Packet<IPlayHandlerClientbound> {

    private int entityId;
    private int effectId;
    private int amplifier;
    private int duration;
    private boolean hideParticles;

    public EntityEffect() {
    }

    public EntityEffect(int entityId, int effectId, int amplifier, int duration, boolean hideParticles) {
        this.entityId = entityId;
        this.effectId = effectId;
        this.amplifier = amplifier;
        this.duration = duration;
        this.hideParticles = hideParticles;
    }

    @Override
    public void read(MCByteBuf buf) {
        entityId = buf.readVarInt();
        effectId = buf.readByte();
        amplifier = buf.readByte();
        duration = buf.readVarInt();
        hideParticles = buf.readBoolean();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeVarInt(entityId);
        buf.writeByte(effectId);
        buf.writeByte(amplifier);
        buf.writeVarInt(duration);
        buf.writeBoolean(hideParticles);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
