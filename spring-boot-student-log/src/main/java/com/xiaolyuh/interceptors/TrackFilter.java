package com.xiaolyuh.interceptors;

import com.xiaolyuh.constants.MdcConstant;
import com.xiaolyuh.core.TrackLoggerFactory;
import org.slf4j.Logger;
import org.slf4j.MDC;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

/**
 * MDC 过滤器
 *
 * @author yuhao.wang3
 */
public class TrackFilter implements Filter {
    Logger logger = TrackLoggerFactory.getLogger(TrackFilter.class);

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        // 为每个请求设置唯一标示到MDC容器中
        setMdc(request);
        try {
            // 执行后续操作
            chain.doFilter(request, response);
        } finally {
            try {
                MDC.remove(MdcConstant.SESSION_KEY);
                MDC.remove(MdcConstant.REQUEST_KEY);
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
    }


    /**
     * 为每个请求设置唯一标示到MDC容器中
     */
    private void setMdc(ServletRequest request) {
        try {
            // 设置SessionId
            String requestId = UUID.randomUUID().toString().replace("-", "");
            MDC.put(MdcConstant.SESSION_KEY, "userId");
            MDC.put(MdcConstant.REQUEST_KEY, requestId);
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }
}