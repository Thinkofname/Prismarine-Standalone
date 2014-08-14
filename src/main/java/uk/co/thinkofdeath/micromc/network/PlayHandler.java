package uk.co.thinkofdeath.micromc.network;

import io.netty.util.concurrent.ScheduledFuture;
import uk.co.thinkofdeath.micromc.chat.TextComponent;
import uk.co.thinkofdeath.micromc.game.Difficulty;
import uk.co.thinkofdeath.micromc.game.Dimension;
import uk.co.thinkofdeath.micromc.game.Gamemode;
import uk.co.thinkofdeath.micromc.log.LogUtil;
import uk.co.thinkofdeath.micromc.network.login.Property;
import uk.co.thinkofdeath.micromc.network.protocol.play.JoinGame;
import uk.co.thinkofdeath.micromc.network.protocol.play.KeepAlivePing;
import uk.co.thinkofdeath.micromc.network.protocol.play.KeepAlivePong;
import uk.co.thinkofdeath.micromc.network.protocol.play.PlayerTeleport;

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
    }

    @Override
    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
        handler.getChannel().closeFuture().addListener(f -> pingTask.cancel(true));
        pingTask = handler.getChannel().eventLoop().scheduleAtFixedRate(() -> {
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
