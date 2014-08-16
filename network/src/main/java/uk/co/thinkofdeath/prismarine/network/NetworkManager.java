package uk.co.thinkofdeath.prismarine.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import uk.co.thinkofdeath.prismarine.log.LogUtil;
import uk.co.thinkofdeath.prismarine.network.protocol.PacketHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.ProtocolDirection;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkManager {

    private static final Logger logger = LogUtil.get(NetworkManager.class);
    private Channel channel;

    private boolean onlineMode = true;
    private KeyPair networkKeyPair;
    private ProtocolDirection incomingPacketType;

    public NetworkManager() {
    }

    public void listen(String address, int port, Class<? extends PacketHandler> initialHandler) {
        incomingPacketType = ProtocolDirection.SERVERBOUND;
        if (isOnlineMode()) {
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

    public void close() {
        logger.info("Disconnecting players");
        channel.close().awaitUninterruptibly();
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
    }

    public KeyPair getNetworkKeyPair() {
        return networkKeyPair;
    }

    public ProtocolDirection getIncomingPacketType() {
        return incomingPacketType;
    }
}
