package uk.co.thinkofdeath.prismarine.server;

import uk.co.thinkofdeath.prismarine.log.LogUtil;
import uk.co.thinkofdeath.prismarine.network.NetworkManager;

import java.security.KeyPair;
import java.util.logging.Logger;

public class PrismarineServer {

    private static final Logger logger = LogUtil.get(PrismarineServer.class);
    private final Configuration config;
    private NetworkManager networkManager = new NetworkManager();

    public PrismarineServer(Configuration config) {
        this.config = config;
        logger.info("Starting Prismarine");
    }

    public void start() {
        networkManager.listen(config.getBindAddress(), config.getPort());
    }

    public void close() {
        logger.info("Shutting down MicroMC");
        networkManager.close();

        logger.info("Shutdown complete");
    }

    public boolean isOnlineMode() {
        return config.isOnlineMode();
    }
}
