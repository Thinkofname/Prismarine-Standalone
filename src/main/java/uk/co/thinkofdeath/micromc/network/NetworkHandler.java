package uk.co.thinkofdeath.micromc.network;

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import uk.co.thinkofdeath.micromc.MicroMC;
import uk.co.thinkofdeath.micromc.chat.Component;
import uk.co.thinkofdeath.micromc.chat.TextComponent;
import uk.co.thinkofdeath.micromc.log.LogUtil;
import uk.co.thinkofdeath.micromc.network.protocol.Packet;
import uk.co.thinkofdeath.micromc.network.protocol.Protocol;
import uk.co.thinkofdeath.micromc.network.protocol.login.LoginDisconnect;
import uk.co.thinkofdeath.micromc.network.protocol.login.SetInitialCompression;

import javax.crypto.SecretKey;
import java.util.logging.Logger;

public class NetworkHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger logger = LogUtil.get(NetworkHandler.class);
    private final MicroMC server;
    private final SocketChannel channel;
    private PacketHandler handler;
    private boolean connected = true;

    public NetworkHandler(MicroMC server, SocketChannel ch, PacketHandler handler) {
        this.server = server;
        channel = ch;
        setHandler(handler);
        ch.closeFuture().addListener(f -> {
            if (connected) {
                logger.info("Disconnected(" + channel.remoteAddress() + "): Connection closed");
            }
            connected = false;
        });
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Packet msg) throws Exception {
        if (connected) {
            logger.info(msg.asString());
            msg.handle(handler);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        disconnect(new TextComponent(cause.getMessage()));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    public void sendPacket(Packet packet) {
        channel.write(packet, channel.voidPromise());
    }

    public void enableEncryption(SecretKey secretKey) {
        channel.pipeline().addBefore("frame-codec", "cipher-codec", new CipherCodec(secretKey));
    }

    public void enableCompression(int threshold) {
        if (channel.pipeline().get(PacketCodec.class).getProtocol() == Protocol.LOGIN) {
            sendPacket(new SetInitialCompression(threshold));
        } else if (channel.pipeline().get(PacketCodec.class).getProtocol() == Protocol.PLAY) {
            // TODO
        } else {
            throw new UnsupportedOperationException();
        }

        CompressionCodec codec = channel.pipeline().get(CompressionCodec.class);
        if (codec == null) {
            codec = new CompressionCodec();
            channel.pipeline().addAfter("frame-codec", "compression-codec", codec);
        }
        codec.setThreshold(threshold);
    }

    public void disconnect(Component reason) {
        logger.info("Disconnected(" + channel.remoteAddress() + "): " + reason.asString());
        Packet packet;
        if (channel.pipeline().get(PacketCodec.class).getProtocol() == Protocol.LOGIN) {
            packet = new LoginDisconnect(reason);
            channel.write(packet)
                    .addListener(ChannelFutureListener.CLOSE);
        } else {
            channel.close();
        }
        connected = false;
        channel.config().setAutoRead(false);
        setHandler(new NullHandler());
    }

    public void setHandler(PacketHandler handler) {
        this.handler = handler;
        handler.setNetworkHandler(this);
    }

    public SocketChannel getChannel() {
        return channel;
    }

    public MicroMC getServer() {
        return server;
    }
}
