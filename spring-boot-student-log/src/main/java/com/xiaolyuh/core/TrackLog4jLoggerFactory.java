package com.xiaolyuh.core;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LocationAwareLogger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Log4jLoggerFactory的包装类
 *
 * @author yuhao.wang3
 */
public class TrackLog4jLoggerFactory implements ILoggerFactory {

    /**
     * key: name (String), value: a Log4jLoggerAdapter;
     */
    Map loggerMap = new ConcurrentHashMap();

    /*
     * (non-Javadoc)
     *
     * @see org.slf4j.ILoggerFactory#getLogger(java.lang.String)
     */
    @Override
    public Logger getLogger(String name) {
        try {
            Logger slf4jLogger = null;
            // protect against concurrent access of loggerMap
            slf4jLogger = (Logger) loggerMap.get(name);
            if (slf4jLogger == null) {
                synchronized (this) {
                    slf4jLogger = (Logger) loggerMap.get(name);
                    if (slf4jLogger == null) {
                        Logger logger = LoggerFactory.getLogger(name);
                        if (logger instanceof LocationAwareLogger) {
                            slf4jLogger = new TrackLogger((LocationAwareLogger) logger);
                        } else {
                            slf4jLogger = logger;
                        }
                        loggerMap.put(name, slf4jLogger);
                    }
                }
            }
            return slf4jLogger;
        } catch (Exception e) {
            return LoggerFactory.getLogger(name);
        }
    }
}