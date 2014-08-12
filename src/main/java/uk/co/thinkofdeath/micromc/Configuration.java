package uk.co.thinkofdeath.micromc;

public class Configuration {

    private String bindAddress;
    private int port;

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
}
