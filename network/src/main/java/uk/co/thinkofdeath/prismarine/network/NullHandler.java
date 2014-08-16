package uk.co.thinkofdeath.prismarine.network;

import uk.co.thinkofdeath.prismarine.network.protocol.PacketHandler;

/**
 * For packets which don't need to be handled
 */
public class NullHandler implements PacketHandler {
    @Override
    public void setNetworkHandler(NetworkHandler handler) {

    }
}
