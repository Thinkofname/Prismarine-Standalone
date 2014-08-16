package uk.co.thinkofdeath.prismarine.network.protocol.play;

import uk.co.thinkofdeath.prismarine.game.Difficulty;
import uk.co.thinkofdeath.prismarine.game.Dimension;
import uk.co.thinkofdeath.prismarine.game.Gamemode;
import uk.co.thinkofdeath.prismarine.network.MCByteBuf;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerClientbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;

public class JoinGame implements Packet<IPlayHandlerClientbound> {

    private int entityId;
    private Gamemode gamemode;
    private boolean hardcore;
    private Dimension dimension;
    private Difficulty difficulty;
    private int maxPlayers;
    private String levelType;
    private boolean reducedDebug;

    public JoinGame() {
    }

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
    public void read(MCByteBuf buf) {
        entityId = buf.readInt();
        int mode = buf.readUnsignedByte();
        if ((mode & 0x8) != 0) {
            hardcore = true;
        }
        gamemode = Gamemode.values()[mode & 0b111];
        dimension = Dimension.byId(buf.readByte());
        difficulty = Difficulty.values()[buf.readUnsignedByte()];
        maxPlayers = buf.readUnsignedByte();
        levelType = buf.readString(255);
        reducedDebug = buf.readBoolean();
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

    @Override
    public void handle(IPlayHandlerClientbound handler) {
        handler.handle(this);
    }
}
