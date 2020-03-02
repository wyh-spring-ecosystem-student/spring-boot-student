package com.xiaolyuh.core;

import com.wlqq.etc.common.log.constants.MdcConstant;
import org.slf4j.MDC;
import org.slf4j.Marker;
import org.slf4j.spi.LocationAwareLogger;

/**
 * 包装原有的Logger对象
 *
 * @author yuhao.wang3
 */
public class TrackLogger implements LocationAwareLogger, java.io.Serializable {

    private LocationAwareLogger logger;

    TrackLogger(LocationAwareLogger logger) {
        this.logger = logger;
    }


    @Override
    public void log(Marker marker, String fqcn, int level, String message, Object[] argArray, Throwable t) {
        logger.log(marker, fqcn, level, mdcValue(message), argArray, t);
    }

    @Override
    public String getName() {
        return logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return logger.isTraceEnabled();
    }

    @Override
    public void trace(String msg) {
        logger.trace(mdcValue(msg));
    }

    @Override
    public void trace(String format, Object arg) {
        logger.trace(mdcValue(format), arg);
    }

    @Override
    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(mdcValue(format), arg1, arg2);
    }

    @Override
    public void trace(String format, Object... arguments) {
        logger.trace(mdcValue(format), arguments);
    }

    @Override
    public void trace(String msg, Throwable t) {
        logger.trace(mdcValue(msg), t);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return logger.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String msg) {
        logger.trace(marker, mdcValue(msg));
    }

    @Override
    public void trace(Marker marker, String format, Object arg) {
        logger.trace(marker, mdcValue(format), arg);
    }

    @Override
    public void trace(Marker marker, String format, Object arg1, Object arg2) {
        logger.trace(marker, mdcValue(format), arg1, arg2);
    }

    @Override
    public void trace(Marker marker, String format, Object... argArray) {
        logger.trace(marker, mdcValue(format), argArray);
    }

    @Override
    public void trace(Marker marker, String msg, Throwable t) {
        logger.trace(marker, mdcValue(msg), t);
    }

    @Override
    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        logger.debug(mdcValue(msg));
    }

    @Override
    public void debug(String format, Object arg) {
        logger.debug(mdcValue(format), arg);
    }

    @Override
    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(mdcValue(format), arg1, arg2);
    }

    @Override
    public void debug(String format, Object... arguments) {
        logger.debug(mdcValue(format), arguments);
    }

    @Override
    public void debug(String msg, Throwable t) {
        logger.debug(mdcValue(msg), t);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String msg) {
        logger.debug(marker, mdcValue(msg));
    }

    @Override
    public void debug(Marker marker, String format, Object arg) {
        logger.debug(marker, mdcValue(format), arg);
    }

    @Override
    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logger.debug(marker, mdcValue(format), arg1, arg2);
    }

    @Override
    public void debug(Marker marker, String format, Object... arguments) {
        logger.debug(marker, mdcValue(format), arguments);
    }

    @Override
    public void debug(Marker marker, String msg, Throwable t) {
        logger.debug(marker, mdcValue(msg), t);
    }

    @Override
    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    @Override
    public void info(String msg) {

        logger.info(mdcValue(msg));
    }

    @Override
    public void info(String format, Object arg) {
        logger.info(mdcValue(format), arg);
    }

    @Override
    public void info(String format, Object arg1, Object arg2) {
        logger.info(mdcValue(format), arg1, arg2);
    }

    @Override
    public void info(String format, Object... arguments) {
        logger.info(mdcValue(format), arguments);
    }

    @Override
    public void info(String msg, Throwable t) {
        logger.info(mdcValue(msg), t);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String msg) {
        logger.info(marker, mdcValue(msg));
    }

    @Override
    public void info(Marker marker, String format, Object arg) {
        logger.info(marker, mdcValue(format), arg);
    }

    @Override
    public void info(Marker marker, String format, Object arg1, Object arg2) {
        logger.info(marker, mdcValue(format), arg1, arg2);
    }

    @Override
    public void info(Marker marker, String format, Object... arguments) {
        logger.info(marker, mdcValue(format), arguments);
    }

    @Override
    public void info(Marker marker, String msg, Throwable t) {
        logger.info(marker, mdcValue(msg), t);
    }

    @Override
    public boolean isWarnEnabled() {
        return logger.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        logger.warn(mdcValue(msg));
    }

    @Override
    public void warn(String format, Object arg) {
        logger.warn(mdcValue(format), arg);
    }

    @Override
    public void warn(String format, Object... arguments) {
        logger.warn(mdcValue(format), arguments);
    }

    @Override
    public void warn(String format, Object arg1, Object arg2) {
        logger.warn(mdcValue(format), arg1, arg2);
    }

    @Override
    public void warn(String msg, Throwable t) {
        logger.warn(mdcValue(msg), t);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return logger.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String msg) {
        logger.warn(marker, mdcValue(msg));
    }

    @Override
    public void warn(Marker marker, String format, Object arg) {
        logger.warn(marker, mdcValue(format), arg);
    }

    @Override
    public void warn(Marker marker, String format, Object arg1, Object arg2) {
        logger.warn(marker, mdcValue(format), arg1, arg2);
    }

    @Override
    public void warn(Marker marker, String format, Object... arguments) {
        logger.warn(marker, mdcValue(format), arguments);
    }

    @Override
    public void warn(Marker marker, String msg, Throwable t) {
        logger.warn(marker, mdcValue(msg), t);
    }

    @Override
    public boolean isErrorEnabled() {
        return logger.isErrorEnabled();
    }

    @Override
    public void error(String msg) {
        logger.error(mdcValue(msg));
    }

    @Override
    public void error(String format, Object arg) {
        logger.error(mdcValue(format), arg);
    }

    @Override
    public void error(String format, Object arg1, Object arg2) {
        logger.error(mdcValue(format), arg1, arg2);
    }

    @Override
    public void error(String format, Object... arguments) {
        logger.error(mdcValue(format), arguments);
    }

    @Override
    public void error(String msg, Throwable t) {
        logger.error(mdcValue(msg), t);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return logger.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String msg) {
        logger.error(marker, mdcValue(msg));
    }

    @Override
    public void error(Marker marker, String format, Object arg) {
        logger.error(marker, mdcValue(format), arg);
    }

    @Override
    public void error(Marker marker, String format, Object arg1, Object arg2) {
        logger.error(marker, mdcValue(format), arg1, arg2);
    }

    @Override
    public void error(Marker marker, String format, Object... arguments) {
        logger.error(marker, mdcValue(format), arguments);
    }

    @Override
    public void error(Marker marker, String msg, Throwable t) {
        logger.error(marker, mdcValue(msg), t);
    }

    /**
     * 输出MDC容器中的值
     *
     * @param msg
     * @return
     */
    private String mdcValue(String msg) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(" [");
            try {
                sb.append(MDC.get(MdcConstant.SESSION_KEY) + ", ");
            } catch (IllegalArgumentException e) {
                sb.append(" , ");
            }

            try {
                sb.append(MDC.get(MdcConstant.REQUEST_KEY));
            } catch (IllegalArgumentException e) {
                sb.append(" ");
            }
            sb.append("] ");
            sb.append(msg);
            return sb.toString();
        } catch (Exception e) {
            return msg;
        }
    }
}
