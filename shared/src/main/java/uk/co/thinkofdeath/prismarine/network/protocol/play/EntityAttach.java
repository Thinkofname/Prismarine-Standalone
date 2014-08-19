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

public class EntityAttach implements Packet<IPlayHandlerClientbound> {

    private int entityId;
    private int vehicleID;
    private boolean leash;

    public EntityAttach() {
    }

    public EntityAttach(int entityId, int vehicleID, boolean leash) {
        this.entityId = entityId;
        this.vehicleID = vehicleID;
        this.leash = leash;
    }

    @Override
    public void read(MCByteBuf buf) {
        entityId = buf.readInt();
        vehicleID = buf.readInt();
        leash = buf.readBoolean();
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(vehicleID);
        buf.writeBoolean(leash);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
