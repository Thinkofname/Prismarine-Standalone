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

import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import uk.co.thinkofdeath.prismarine.chat.Component;
import uk.co.thinkofdeath.prismarine.chat.TextComponent;
import uk.co.thinkofdeath.prismarine.log.LogUtil;
import uk.co.thinkofdeath.prismarine.network.protocol.Packet;
import uk.co.thinkofdeath.prismarine.network.protocol.PacketHandler;
import uk.co.thinkofdeath.prismarine.network.protocol.Protocol;
import uk.co.thinkofdeath.prismarine.network.protocol.ProtocolDirection;
import uk.co.thinkofdeath.prismarine.network.protocol.login.LoginDisconnect;
import uk.co.thinkofdeath.prismarine.network.protocol.login.SetInitialCompression;

import javax.crypto.SecretKey;
import java.util.logging.Logger;

public class NetworkHandler extends SimpleChannelInboundHandler<Packet> {

    private static final Logger logger = LogUtil.get(NetworkHandler.class);
    private final NetworkManager manager;
    private final SocketChannel channel;
    private PacketHandler handler;
    private boolean connected = true;

    public NetworkHandler(NetworkManager manager, SocketChannel ch, PacketHandler handler) {
        this.manager = manager;
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
    @SuppressWarnings("unchecked")
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
        if (manager.getIncomingPacketType() == ProtocolDirection.SERVERBOUND) {
            // Only servers can change the threshold for others
            if (channel.pipeline().get(PacketCodec.class).getProtocol() == Protocol.LOGIN) {
                sendPacket(new SetInitialCompression(threshold));
            } else if (channel.pipeline().get(PacketCodec.class).getProtocol() == Protocol.PLAY) {
                // TODO
            } else {
                throw new UnsupportedOperationException();
            }
        }

        CompressionCodec codec = channel.pipeline().get(CompressionCodec.class);
        if (threshold != -1) {
            if (codec == null) {
                codec = new CompressionCodec();
                channel.pipeline().addAfter("frame-codec", "compression-codec", codec);
            }
            codec.setThreshold(threshold);
        } else {
            if (channel.pipeline().get(CompressionCodec.class) != null) {
                channel.pipeline().remove(CompressionCodec.class);
            }
        }
    }

    public void disconnect(Component reason) {
        logger.info("Disconnected(" + channel.remoteAddress() + "): " + reason.asString());
        Packet packet;

        if (manager.getIncomingPacketType() == ProtocolDirection.SERVERBOUND) {
            if (channel.pipeline().get(PacketCodec.class).getProtocol() == Protocol.LOGIN) {
                packet = new LoginDisconnect(reason);
                channel.write(packet)
                        .addListener(ChannelFutureListener.CLOSE);
            } else {
                channel.close();
            }
        } else {
            // Client doesn't send a disconnect message
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

    public NetworkManager getManager() {
        return manager;
    }
}
