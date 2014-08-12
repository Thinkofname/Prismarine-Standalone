package uk.co.thinkofdeath.micromc;

import uk.co.thinkofdeath.micromc.log.LogUtil;
import uk.co.thinkofdeath.micromc.network.NetworkManager;

import java.util.logging.Logger;

public class MicroMC {

    private static final Logger logger = LogUtil.get(MicroMC.class);
    private final Configuration config;
    private NetworkManager networkManager = new NetworkManager(this);

    public MicroMC(Configuration config) {
        this.config = config;
        logger.info("Starting MicroMC");
    }

    public void start() {
        networkManager.listen(config.getBindAddress(), config.getPort());
    }

    public void close() {
        logger.info("Shutting down MicroMC");
        networkManager.close();

        logger.info("Shutdown complete");
    }
}
