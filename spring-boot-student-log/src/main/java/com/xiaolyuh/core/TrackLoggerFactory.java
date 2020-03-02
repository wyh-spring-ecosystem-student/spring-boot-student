
package com.xiaolyuh.core;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;


/**
 * 装饰原有的LoggerFactory对象
 *
 * @author yuhao.wang3
 */
public final class TrackLoggerFactory {
    private static ILoggerFactory iLoggerFactory = new TrackLog4jLoggerFactory();

    public static Logger getLogger(Class aClass) {
        return iLoggerFactory.getLogger(aClass.getName());
    }

    public static Logger getLogger(String name) {
        return iLoggerFactory.getLogger(name);
    }
}
