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

package uk.co.thinkofdeath.prismarine.server.net;

import uk.co.thinkofdeath.prismarine.chat.Color;
import uk.co.thinkofdeath.prismarine.chat.Component;
import uk.co.thinkofdeath.prismarine.chat.TextComponent;
import uk.co.thinkofdeath.prismarine.network.Constants;
import uk.co.thinkofdeath.prismarine.network.NetworkHandler;
import uk.co.thinkofdeath.prismarine.network.ping.Ping;
import uk.co.thinkofdeath.prismarine.network.protocol.IStatusHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusPing;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusPong;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusReponse;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusRequest;

public class StatusHandler implements IStatusHandlerServerbound {

    private NetworkHandler handler;
    private State currentState = State.WAITING_REQUEST;

    @Override
    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(StatusRequest statusRequest) {
        require(State.WAITING_REQUEST);

        Ping ping = new Ping();
        Component motd = new TextComponent("Hello ");
        motd.setColor(Color.GREEN);
        Component world = new TextComponent("world");
        world.setColor(Color.BLUE);
        motd.addComponent(world);
        ping.setDescription(motd);
        ping.getVersion().setName("MicroMC - " + Constants.MINECRAFT_VERSION);
        ping.getVersion().setProtocol(Constants.PROTOCOL_VERSION);
        ping.getPlayers().setMax(20);

        handler.sendPacket(new StatusReponse(ping));
        currentState = State.WAITING_PING;
    }

    @Override
    public void handle(StatusPing statusPing) {
        require(State.WAITING_PING);
        handler.getChannel().write(new StatusPong(statusPing.getTime()))
                .addListener(f -> handler.getChannel().close());
        currentState = State.DONE;
    }

    private void require(State state) {
        if (state != currentState) {
            throw new RuntimeException("Incorrect state");
        }
    }

    private static enum State {
        WAITING_REQUEST,
        WAITING_PING,
        DONE
    }
}
