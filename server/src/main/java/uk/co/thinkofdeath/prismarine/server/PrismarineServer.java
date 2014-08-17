/*
 * Copyright 2014 Matthew Collins
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.thinkofdeath.prismarine.server;

import uk.co.thinkofdeath.prismarine.Prismarine;
import uk.co.thinkofdeath.prismarine.log.LogUtil;
import uk.co.thinkofdeath.prismarine.server.network.HandshakingHandler;

import java.util.logging.Logger;

public class PrismarineServer extends Prismarine {

    private static final Logger logger = LogUtil.get(PrismarineServer.class);
    private final Configuration config;

    public PrismarineServer(Configuration config) {
        this.config = config;
        logger.info("Starting Prismarine");
        init();
    }

    public void start() {
        getNetworkManager().listen(config.getBindAddress(), config.getPort(), HandshakingHandler::new);
    }

    public void close() {
        logger.info("Shutting down Prismarine");
        getNetworkManager().close();

        logger.info("Shutdown complete");
    }

    public boolean isOnlineMode() {
        return config.isOnlineMode();
    }
}
