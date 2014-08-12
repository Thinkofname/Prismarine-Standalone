package uk.co.thinkofdeath.micromc.log;

import java.util.logging.Logger;

public class LogUtil {

    public static Logger get(Class<?> clazz) {
        return Logger.getLogger(clazz.getSimpleName());
    }
}
