package uk.co.thinkofdeath.prismarine.network;

import io.netty.util.concurrent.ScheduledFuture;
import uk.co.thinkofdeath.prismarine.chat.TextComponent;
import uk.co.thinkofdeath.prismarine.game.Difficulty;
import uk.co.thinkofdeath.prismarine.game.Dimension;
import uk.co.thinkofdeath.prismarine.game.Gamemode;
import uk.co.thinkofdeath.prismarine.log.LogUtil;
import uk.co.thinkofdeath.prismarine.network.login.Property;
import uk.co.thinkofdeath.prismarine.network.protocol.play.JoinGame;
import uk.co.thinkofdeath.prismarine.network.protocol.play.KeepAlivePing;
import uk.co.thinkofdeath.prismarine.network.protocol.play.KeepAlivePong;
import uk.co.thinkofdeath.prismarine.network.protocol.play.PlayerTeleport;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PlayHandler implements PacketHandler {

    private static final Logger logger = LogUtil.get(PlayHandler.class);
    private final String username;
    private final UUID uuid;
    private final Property[] properties;
    private NetworkHandler handler;

    private ScheduledFuture<?> pingTask;
    private Random random = new Random();
    private int lastPingId = -1;

    public PlayHandler(String username, UUID uuid, Property[] properties) {
        this.username = username;
        this.uuid = uuid;
        this.properties = properties;
    }

    public void handle(KeepAlivePong keepAlivePong) {
        if (lastPingId != keepAlivePong.getId()) {
            handler.disconnect(new TextComponent("Incorrect Keep Alive"));
        }
        lastPingId = -1;
    }

    @Override
    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
        handler.getChannel().closeFuture().addListener(f -> pingTask.cancel(true));
        pingTask = handler.getChannel().eventLoop().scheduleAtFixedRate(() -> {
            if (lastPingId != -1) {
                handler.disconnect(new TextComponent("Timed out"));
                return;
            }
            lastPingId = random.nextInt(Short.MAX_VALUE);
            handler.sendPacket(new KeepAlivePing(lastPingId));
        }, 5, 10, TimeUnit.SECONDS);
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
