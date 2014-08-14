package uk.co.thinkofdeath.micromc.network.ping;

import uk.co.thinkofdeath.micromc.chat.Component;

public class Ping {

    private final PingVersion version = new PingVersion();
    private final PingPlayers players = new PingPlayers();
    private Component description;

    public Component getDescription() {
        return description;
    }

    public void setDescription(Component description) {
        this.description = description;
    }

    public PingVersion getVersion() {
        return version;
    }

    public PingPlayers getPlayers() {
        return players;
    }
}
