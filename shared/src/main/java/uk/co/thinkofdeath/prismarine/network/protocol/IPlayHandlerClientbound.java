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

import uk.co.thinkofdeath.prismarine.network.protocol.play.*;

public interface IPlayHandlerClientbound extends PacketHandler {
    void handle(JoinGame joinGame);

    void handle(KeepAlivePing keepAlivePing);

    void handle(PlayerTeleport playerTeleport);

    void handle(ServerMessage serverMessage);

    void handle(TimeUpdate timeUpdate);

    void handle(EntityEquipment entityEquipment);

    void handle(SpawnPosition spawnPosition);

    void handle(UpdateHealth updateHealth);

    void handle(Respawn respawn);

    void handle(SetHeldItem setHeldItem);

    void handle(UseBed useBed);

    void handle(Animation animation);

    void handle(SpawnPlayer spawnPlayer);

    void handle(CollectItem collectItem);

    void handle(SpawnObject spawnObject);

    void handle(SpawnLivingEntity spawnLivingEntity);

    void handle(SpawnPainting spawnPainting);

    void handle(SpawnExperienceOrb spawnExperienceOrb);

    void handle(EntityVelocity entityVelocity);

    void handle(DestroyEntities destroyEntities);

    void handle(Entity entity);

    void handle(EntityMove entityMove);

    void handle(EntityLook entityLook);

    void handle(EntityMoveLook entityMoveLook);

    void handle(EntityTeleport entityTeleport);

    void handle(EntityHeadLook entityHeadLook);

    void handle(EntityStatus entityStatus);

    void handle(EntityAttach entityAttach);

    void handle(EntitySetMetadata entitySetMetadata);

    void handle(EntityEffect entityEffect);

    void handle(EntityRemoveEffect entityRemoveEffect);

    void handle(SetExperience setExperience);
}
