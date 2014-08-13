package uk.co.thinkofdeath.micromc.network;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import uk.co.thinkofdeath.micromc.log.LogUtil;
import uk.co.thinkofdeath.micromc.network.protocol.Protocol;

import java.util.logging.Logger;

public class ConnectionInitializer extends ChannelInitializer<SocketChannel> {

    private static final Logger logger = LogUtil.get(ConnectionInitializer.class);
    private final NetworkManager networkManager;

    public ConnectionInitializer(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        logger.info("Connection: " + ch.remoteAddress());

        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("timeout", new ReadTimeoutHandler(30));
        pipeline.addLast("frame-codec", new VarIntFrameCodec());
        pipeline.addLast("packet-codec", new PacketCodec(Protocol.HANDSHAKING));
        pipeline.addLast("handler", new NetworkHandler(networkManager.getServer(), ch, new HandshakingHandler()));
    }
}
