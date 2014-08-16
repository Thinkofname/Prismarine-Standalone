package uk.co.thinkofdeath.prismarine.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import uk.co.thinkofdeath.prismarine.server.PrismarineServer;
import uk.co.thinkofdeath.prismarine.log.LogUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkManager {

    private static final Logger logger = LogUtil.get(NetworkManager.class);
    private final PrismarineServer server;
    private Channel channel;

    public NetworkManager(PrismarineServer server) {
        this.server = server;
    }

    public void listen(String address, int port) {
        logger.log(Level.INFO, "Starting on {0}:{1,number,#}",
                new Object[]{address, port});

        final EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ConnectionInitializer(this))
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
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

    public PrismarineServer getServer() {
        return server;
    }
}
