package uk.co.thinkofdeath.prismarine.network;

/**
 * For packets which don't need to be handled
 */
public class NullHandler implements PacketHandler {
    @Override
    public void setNetworkHandler(NetworkHandler handler) {

    }
}
