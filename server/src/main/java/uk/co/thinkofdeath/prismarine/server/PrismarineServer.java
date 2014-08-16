package uk.co.thinkofdeath.prismarine.server;

import uk.co.thinkofdeath.prismarine.log.LogUtil;
import uk.co.thinkofdeath.prismarine.network.NetworkManager;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class PrismarineServer {

    public static final String MINECRAFT_VERSION = "14w33c";
    public static final int PROTOCOL_VERSION = 39;

    private static final Logger logger = LogUtil.get(PrismarineServer.class);
    private final Configuration config;
    private NetworkManager networkManager = new NetworkManager(this);
    private KeyPair networkKeyPair;

    public PrismarineServer(Configuration config) {
        this.config = config;
        logger.info("Starting Prismarine");

        if (isOnlineMode()) {
            logger.info("Generating encryption keys");
            try {
                KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
                generator.initialize(1024);
                networkKeyPair = generator.generateKeyPair();
            } catch (NoSuchAlgorithmException e) {
                logger.info("Failed to generate encryption keys");
                throw new RuntimeException(e);
            }
        }
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

    public KeyPair getNetworkKeyPair() {
        return networkKeyPair;
    }
}
