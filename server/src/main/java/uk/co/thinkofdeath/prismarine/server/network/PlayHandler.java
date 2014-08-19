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

package uk.co.thinkofdeath.prismarine.server.network;

import io.netty.util.concurrent.ScheduledFuture;
import uk.co.thinkofdeath.prismarine.chat.Color;
import uk.co.thinkofdeath.prismarine.chat.TextComponent;
import uk.co.thinkofdeath.prismarine.game.Difficulty;
import uk.co.thinkofdeath.prismarine.game.Dimension;
import uk.co.thinkofdeath.prismarine.game.GameMode;
import uk.co.thinkofdeath.prismarine.log.LogUtil;
import uk.co.thinkofdeath.prismarine.network.NetworkHandler;
import uk.co.thinkofdeath.prismarine.network.login.Property;
import uk.co.thinkofdeath.prismarine.network.protocol.IPlayHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.play.*;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PlayHandler implements IPlayHandlerServerbound {

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

    @Override
    public void handle(KeepAlivePong keepAlivePong) {
        if (lastPingId != keepAlivePong.getId()) {
            handler.disconnect(new TextComponent("Incorrect Keep Alive"));
        }
        lastPingId = -1;
    }

    @Override
    public void handle(ChatMessage chatMessage) {
        TextComponent component = new TextComponent("<");
        TextComponent user = new TextComponent(username);
        user.setColor(Color.AQUA);
        component.addComponent(user);
        component.addComponent(new TextComponent("> "));
        component.addComponent(new TextComponent(chatMessage.getMessage()));
        handler.sendPacket(new ServerMessage(
                component,
                ServerMessage.Type.CHAT
        ));
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
                GameMode.CREATIVE,
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
