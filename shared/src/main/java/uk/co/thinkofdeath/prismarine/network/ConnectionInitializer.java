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

package uk.co.thinkofdeath.prismarine.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import uk.co.thinkofdeath.prismarine.log.LogUtil;
import uk.co.thinkofdeath.prismarine.network.protocol.PacketHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Protocol;

import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * Initializes the connection for new clients
 */
public class ConnectionInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger logger = LogUtil.get(ConnectionInitializer.class);
    private final NetworkManager networkManager;
    private final Supplier<PacketHandler> initialHandler;

    /**
     * Creates a ConnectionInitializer which will setup the pipeline
     * for the channel. The PacketCodec will use the handler created
     * by the initialHandler supplier
     *
     * @param networkManager
     *         the network manager which owns this
     * @param initialHandler
     *         the supplier which creates the initial packet handler
     */
    public ConnectionInitializer(NetworkManager networkManager, Supplier<PacketHandler> initialHandler) {
        this.networkManager = networkManager;
        this.initialHandler = initialHandler;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("Connection(" + ch.remoteAddress() + ")");

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("timeout", new ReadTimeoutHandler(30));
        pipeline.addLast("frame-codec", new VarIntFrameCodec());
        pipeline.addLast("packet-codec", new PacketCodec(Protocol.HANDSHAKING, networkManager.getIncomingPacketType()));
        pipeline.addLast("handler", new NetworkHandler(networkManager, ch, initialHandler.get()));
    }
}
