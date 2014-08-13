package uk.co.thinkofdeath.micromc.network;

import uk.co.thinkofdeath.micromc.network.ping.Ping;
import uk.co.thinkofdeath.micromc.network.protocol.status.StatusPing;
import uk.co.thinkofdeath.micromc.network.protocol.status.StatusPong;
import uk.co.thinkofdeath.micromc.network.protocol.status.StatusReponse;
import uk.co.thinkofdeath.micromc.network.protocol.status.StatusRequest;

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
        ping.setDescription("Hello world");
        ping.getVersion().setName("MicroMC - 14w32d");
        ping.getVersion().setProtocol(36);
        ping.getPlayers().setMax(20);

        handler.getChannel()
                .writeAndFlush(new StatusReponse(ping));
        currentState = State.WAITING_PING;
    }

    public void handle(StatusPing statusPing) {
        require(State.WAITING_PING);
        handler.getChannel().writeAndFlush(new StatusPong(statusPing.getTime()))
                .addListener(f -> handler.getChannel().close());
        currentState = State.DONE;
    }

    private void require(State state) {
        if (state != currentState) {
            throw new RuntimeException("Incorrect state");
        }
    }

    static enum State {
        WAITING_REQUEST,
        WAITING_PING,
        DONE
    }
}
