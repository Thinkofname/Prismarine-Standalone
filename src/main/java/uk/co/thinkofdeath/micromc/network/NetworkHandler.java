package uk.co.thinkofdeath.micromc.network;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import uk.co.thinkofdeath.micromc.log.LogUtil;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;

import java.util.logging.Logger;

public class NetworkHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger logger = LogUtil.get(NetworkHandler.class);
    private final SocketChannel channel;
    private PacketHandler handler;

    public NetworkHandler(SocketChannel ch, PacketHandler handler) {
        channel = ch;
        setHandler(handler);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet msg) throws Exception {
        logger.info(msg.toString());
        msg.handle(handler);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }

    public void setHandler(PacketHandler handler) {
        this.handler = handler;
        handler.setNetworkHandler(this);
    }

    public SocketChannel getChannel() {
        return channel;
    }
}
