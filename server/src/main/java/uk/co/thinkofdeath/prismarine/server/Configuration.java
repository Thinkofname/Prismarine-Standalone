package uk.co.thinkofdeath.prismarine.server;

public class Configuration {

    private String bindAddress = "0.0.0.0";
    private int port = 25565;
    private boolean onlineMode = true;

    public Configuration() {

    }

    public String getBindAddress() {
        return bindAddress;
    }

    public void setBindAddress(String bindAddress) {
        this.bindAddress = bindAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }

    public void setOnlineMode(boolean onlineMode) {
        this.onlineMode = onlineMode;
    }
}
