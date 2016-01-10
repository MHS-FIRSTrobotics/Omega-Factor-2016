package org.omegafactor.robot.logger;

import java.util.Optional;
import java.util.logging.*;
import java.util.logging.Logger;

/**
 * Created by FIRST on 1/9/2016.
 */
public final class Log {
    private final static Log instance = new Log();

    public static java.util.logging.Logger getInstance(String name) {
        return Logger.getLogger(name);
    }

    public static Log log(Level level, String TAG, String message) {
        Logger.getLogger(TAG).log(level, message);
        return instance;
    }

    public static Log log(Level level, String tag, String message, final Throwable throwable) {
        Logger.getLogger(tag).log(level, message, throwable);
        return instance;
    }

    public static Log d(String tag, String message, final Throwable throwable) {
        log(Level.FINE, tag, message, throwable);
        return instance;
    }

    public static Log i(String tag, String message, final Throwable throwable) {
        log(Level.INFO, tag, message, throwable);
        return instance;
    }

    public static Log w(String tag, String message, final Throwable throwable) {
        log(Level.WARNING, tag, message, throwable);
        return instance;
    }

    public static Log e(String tag, String message, final Throwable throwable) {
        log(Level.SEVERE, tag, message, throwable);
        return instance;
    }

    public static Log wtf(String tag, String message, final Throwable throwable) {
        log(Level.SEVERE, tag, "A Failure Occurred:\n" + message, throwable);
        return instance;
    }

    public static Log d(String tag, String message) {
        log(Level.FINE, tag, message);
        return instance;
    }

    public static Log i(String tag, String message) {
        log(Level.INFO, tag, message);
        return instance;
    }

    public static Log w(String tag, String message) {
        log(Level.WARNING, tag, message);
        return instance;
    }

    public static Log e(String tag, String message) {
        log(Level.SEVERE, tag, message);
        return instance;
    }

    public static Log wtf(String tag, String message) {
        log(Level.SEVERE, tag, "A Failure Occurred:\n" + message);
        return instance;
    }
}
