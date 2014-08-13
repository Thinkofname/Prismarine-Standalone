package uk.co.thinkofdeath.micromc;

import uk.co.thinkofdeath.micromc.log.LogUtil;
import uk.co.thinkofdeath.micromc.network.NetworkManager;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class MicroMC {

    public static final String MINECRAFT_VERSION = "14w32d";
    public static final int PROTOCOL_VERSION = 36;

    private static final Logger logger = LogUtil.get(MicroMC.class);
    private final Configuration config;
    private NetworkManager networkManager = new NetworkManager(this);
    private KeyPair networkKeyPair;

    public MicroMC(Configuration config) {
        this.config = config;
        logger.info("Starting MicroMC");

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
