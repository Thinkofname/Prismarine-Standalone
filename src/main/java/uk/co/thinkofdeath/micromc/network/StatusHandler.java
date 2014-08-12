package uk.co.thinkofdeath.micromc.network;

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
        handler.getChannel()
                .writeAndFlush(new StatusReponse(
                        "{\"version\":{\"name\":\"MicroMC - 14w32d\", \"protocol\": 36}," +
                                "\"players\":{\"max\":1,\"online\": 0}," +
                                "\"description\":\"Banana\"" +
                                "}"
                ));
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
