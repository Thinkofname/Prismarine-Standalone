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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import uk.co.thinkofdeath.prismarine.Prismarine;
import uk.co.thinkofdeath.prismarine.log.LogUtil;
import uk.co.thinkofdeath.prismarine.network.protocol.PacketHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.ProtocolDirection;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The core network handler. This handles listening for connections
 * (for a server) or connecting to a server (for clients)
 */
public class NetworkManager {

    private static final Logger logger = LogUtil.get(NetworkManager.class);
    private final Prismarine prismarine;
    private Channel channel;

    private boolean onlineMode = true;
    private KeyPair networkKeyPair;
    private ProtocolDirection incomingPacketType;

    /**
     * Creates a new Network Manager
     *
     * @param prismarine
     *         the prismarine instance
     */
    public NetworkManager(Prismarine prismarine) {
        this.prismarine = prismarine;
    }

    /**
     * Start listening on the specified address &amp; port. The initial handler for
     * each connection will be created from the supplier
     *
     * @param address
     *         the address of the server
     * @param port
     *         the port of the server
     * @param initialHandler
     *         the supplier which creates the initial packet handler
     */
    public void listen(String address, int port, Supplier<PacketHandler> initialHandler) {
        // Listening == Server
        incomingPacketType = ProtocolDirection.SERVERBOUND;
        if (isOnlineMode()) {
            // Encryption is only enabled in online mode
            logger.info("Generating encryption keys");
            try {
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(1024);
                networkKeyPair = generator.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                logger.info("Failed to generate encryption keys");
                throw new RuntimeException(e);
            }
        }

        logger.log(Level.INFO, "Starting on {0}:{1,number,#}",
                new Object[]{address, port});

        final EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childHandler(new ConnectionInitializer(this, initialHandler))
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.TCP_NODELAY, true);

        channel = bootstrap.bind(address, port)
                .channel();
        channel.closeFuture()
                .addListener(future -> group.shutdownGracefully());

    }

    /**
     * Disconnects the server/client
     */
    public void close() {
        logger.info("Disconnecting...");
        channel.close().awaitUninterruptibly();
    }

    /**
     * Returns whether the manager is in online mode or not
     *
     * @return whether this is in online mode
     */
    public boolean isOnlineMode() {
        return onlineMode;
    }

    /**
     * Enables/Disables online mode for the manager
     *
     * @param onlineMode
     *         the new online mode state
     */
    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
    }

    /**
     * Returns the key pair used for encryption.
     * This is null in offline mode
     *
     * @return the encryption key pair or null
     */
    public KeyPair getNetworkKeyPair() {
        return networkKeyPair;
    }

    /**
     * Returns the incoming packet direction for the manager.
     * This is null if a connection hasn't been started yet
     *
     * @return the incoming packet direction or null
     */
    public ProtocolDirection getIncomingPacketType() {
        return incomingPacketType;
    }

    public Prismarine getPrismarine() {
        return prismarine;
    }
}
