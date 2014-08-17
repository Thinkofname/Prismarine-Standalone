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

import uk.co.thinkofdeath.prismarine.chat.TextComponent;
import uk.co.thinkofdeath.prismarine.network.Constants;
import uk.co.thinkofdeath.prismarine.network.NetworkHandler;
import uk.co.thinkofdeath.prismarine.network.PacketCodec;
import uk.co.thinkofdeath.prismarine.network.protocol.IHandshakingHandlerServerbound;
import uk.co.thinkofdeath.prismarine.network.protocol.Protocol;
import uk.co.thinkofdeath.prismarine.network.protocol.handshaking.Handshake;

public class HandshakingHandler implements IHandshakingHandlerServerbound {

    private NetworkHandler handler;

    @Override
    public void handle(Handshake handshake) {
        switch (handshake.getNext()) {
            case STATUS:
                handler.getChannel().pipeline().get(PacketCodec.class)
                        .setProtocol(Protocol.STATUS);
                handler.setHandler(new StatusHandler());
                break;
            case LOGIN:
                handler.getChannel().pipeline().get(PacketCodec.class)
                        .setProtocol(Protocol.LOGIN);
                handler.setHandler(new LoginHandler());

                if (handshake.getProtocolVersion() < Constants.PROTOCOL_VERSION) {
                    handler.disconnect(new TextComponent("Client out of date. This server is using " + Constants.MINECRAFT_VERSION
                            + " (" + handshake.getProtocolVersion() + ":" + Constants.PROTOCOL_VERSION + ")"));
                    return;
                } else if (handshake.getProtocolVersion() > Constants.PROTOCOL_VERSION) {
                    handler.disconnect(new TextComponent("Server out of data. This server is using " + Constants.MINECRAFT_VERSION
                            + " (" + handshake.getProtocolVersion() + ":" + Constants.PROTOCOL_VERSION + ")"));
                    return;
                }
                break;
            default:
                throw new RuntimeException("Unknown state");
        }
    }

    @Override
    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
    }
}
