package uk.co.thinkofdeath.micromc.network;

import uk.co.thinkofdeath.micromc.game.Difficulty;
import uk.co.thinkofdeath.micromc.game.Dimension;
import uk.co.thinkofdeath.micromc.game.Gamemode;
import uk.co.thinkofdeath.micromc.network.login.Property;
import uk.co.thinkofdeath.micromc.network.protocol.play.JoinGame;
import uk.co.thinkofdeath.micromc.network.protocol.play.PlayerTeleport;

import java.util.UUID;

public class PlayHandler implements PacketHandler {

    private final String username;
    private final UUID uuid;
    private final Property[] properties;
    private NetworkHandler handler;

    public PlayHandler(String username, UUID uuid, Property[] properties) {
        this.username = username;
        this.uuid = uuid;
        this.properties = properties;
    }

    @Override
    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
    }

    public void join() {
        handler.sendPacket(new JoinGame(
                0,
                Gamemode.CREATIVE,
                false,
                Dimension.OVERWORLD,
                Difficulty.EASY,
                20,
                "",
                false
        ));
        handler.sendPacket(new PlayerTeleport(
                0, 128, 0,
                0, 0,
                0
        ));
    }
}
