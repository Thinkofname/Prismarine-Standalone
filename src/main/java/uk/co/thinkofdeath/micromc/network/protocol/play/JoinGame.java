package uk.co.thinkofdeath.micromc.network.protocol.play;

import uk.co.thinkofdeath.micromc.game.Difficulty;
import uk.co.thinkofdeath.micromc.game.Dimension;
import uk.co.thinkofdeath.micromc.game.Gamemode;
import uk.co.thinkofdeath.micromc.network.MCByteBuf;
import uk.co.thinkofdeath.micromc.network.NullHandler;
import uk.co.thinkofdeath.micromc.network.PlayHandler;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

public class JoinGame implements Packet<NullHandler> {

    private int entityId;
    private Gamemode gamemode;
    private boolean hardcore;
    private Dimension dimension;
    private Difficulty difficulty;
    private int maxPlayers;
    private String levelType;
    private boolean reducedDebug;

    public JoinGame(int entityId, Gamemode gamemode, boolean hardcore,
                    Dimension dimension, Difficulty difficulty,
                    int maxPlayers, String levelType, boolean reducedDebug) {
        this.entityId = entityId;
        this.gamemode = gamemode;
        this.hardcore = hardcore;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.levelType = levelType;
        this.reducedDebug = reducedDebug;
    }

    @Override
    public void write(MCByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeByte(gamemode.ordinal() | (hardcore ? 0x8 : 0x0));
        buf.writeByte(dimension.getId());
        buf.writeByte(difficulty.ordinal());
        buf.writeByte(maxPlayers);
        buf.writeString(levelType);
        buf.writeBoolean(reducedDebug);
    }
}
