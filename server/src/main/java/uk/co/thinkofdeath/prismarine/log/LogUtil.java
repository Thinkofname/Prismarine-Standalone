package uk.co.thinkofdeath.prismarine.log;

import java.util.logging.Logger;

public class LogUtil {

    public static Logger get(Class<?> clazz) {
        return Logger.getLogger(clazz.getSimpleName());
    }
}
