package uk.co.thinkofdeath.prismarine.network;

import uk.co.thinkofdeath.prismarine.chat.Color;
import uk.co.thinkofdeath.prismarine.chat.Component;
import uk.co.thinkofdeath.prismarine.chat.TextComponent;
import uk.co.thinkofdeath.prismarine.network.ping.Ping;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusPing;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusPong;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusReponse;
import uk.co.thinkofdeath.prismarine.network.protocol.status.StatusRequest;

public class StatusHandler implements PacketHandler {

    private NetworkHandler handler;
    private State currentState = State.WAITING_REQUEST;

    @Override
    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
    }

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
