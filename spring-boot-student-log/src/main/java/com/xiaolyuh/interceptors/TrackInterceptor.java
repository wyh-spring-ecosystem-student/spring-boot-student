package com.xiaolyuh.interceptors;

import com.xiaolyuh.constants.MdcConstant;
import com.xiaolyuh.core.TrackLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * MDC 过滤器
 *
 * @author yuhao.wang3
 */
public class TrackInterceptor extends HandlerInterceptorAdapter {
    Logger logger = TrackLoggerFactory.getLogger(TrackInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        setMdc(request);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        deleteMdc();
    }

    /**
     * 为每个请求设置唯一标示到MDC容器中
     */
    private void setMdc(HttpServletRequest request) {
        try {
            // 设置SessionId
            String requestId = UUID.randomUUID().toString().replace("-", "");
            MDC.put(MdcConstant.SESSION_KEY, "userId");
            MDC.put(MdcConstant.REQUEST_KEY, requestId);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    /**
     * 删除MDC标示
     */
    private void deleteMdc() {
        try {
            MDC.remove(MdcConstant.SESSION_KEY);
            MDC.remove(MdcConstant.REQUEST_KEY);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}
