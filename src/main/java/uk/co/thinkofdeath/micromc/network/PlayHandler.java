package uk.co.thinkofdeath.micromc.network;

public class PlayHandler implements PacketHandler {

    private NetworkHandler handler;

    @Override
    public void setNetworkHandler(NetworkHandler handler) {
        this.handler = handler;
    }
}
