package uk.co.thinkofdeath.micromc.network.ping;

public class Ping {

    private final PingVersion version = new PingVersion();
    private final PingPlayers players = new PingPlayers();
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PingVersion getVersion() {
        return version;
    }

    public PingPlayers getPlayers() {
        return players;
    }
}
