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

public class UpdateHealth implements Packet<IPlayHandlerClientbound> {

    private float health;
    private int food;
    private float saturation;

    public UpdateHealth() {
    }

    public UpdateHealth(float health, int food, float saturation) {
        this.health = health;
        this.food = food;
        this.saturation = saturation;
    }

    @Override
    public void read(MCByteBuf buf) {
        health = buf.readFloat();
        food = buf.readVarInt();
        saturation = buf.readFloat();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeFloat(health);
        buf.writeVarInt(food);
        buf.writeFloat(saturation);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
