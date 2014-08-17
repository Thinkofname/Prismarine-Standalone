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

package uk.co.thinkofdeath.prismarine.network.protocol.status;

import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IStatusHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class StatusPong implements Packet<IStatusHandlerClientbound> {

    private long time;

    public StatusPong() {
    }

    public StatusPong(long time) {
        this.time = time;
    }

    @Override
    public void read(MCByteBuf buf) {
        time = buf.readLong();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeLong(time);
    }

    @Override
    public void handle(IStatusHandlerClientbound handler) {
        handler.handle(this);
    }
}
