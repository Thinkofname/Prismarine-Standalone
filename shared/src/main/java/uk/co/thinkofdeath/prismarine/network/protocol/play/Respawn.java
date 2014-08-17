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

import uk.co.thinkofdeath.prismarine.game.Difficulty;
import uk.co.thinkofdeath.prismarine.game.Dimension;
import uk.co.thinkofdeath.prismarine.game.GameMode;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class Respawn implements Packet<IPlayHandlerClientbound> {

    private Dimension dimension;
    private Difficulty difficulty;
    private GameMode gameMode;
    private String levelType;

    public Respawn() {
    }

    public Respawn(Dimension dimension, Difficulty difficulty, GameMode gameMode, String levelType) {
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.levelType = levelType;
    }

    @Override
    public void read(MCByteBuf buf) {
        dimension = Dimension.byId(buf.readInt());
        gameMode = GameMode.values()[buf.readUnsignedByte()];
        difficulty = Difficulty.values()[buf.readUnsignedByte()];
        levelType = buf.readString(255);
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeInt(dimension.getId());
        buf.writeByte(gameMode.ordinal());
        buf.writeByte(difficulty.ordinal());
        buf.writeString(levelType);
    }

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
