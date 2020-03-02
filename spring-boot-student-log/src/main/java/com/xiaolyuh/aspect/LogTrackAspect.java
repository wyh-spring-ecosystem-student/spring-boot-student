package com.xiaolyuh.aspect;


import com.xiaolyuh.constants.MdcConstant;
import com.xiaolyuh.core.TrackLoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * 设置在输出日志钱需要设置sessionID到MDC容器(占时没用)
 *
 * @author yuhao.wang3
 */
@Aspect
public class LogTrackAspect {
    private static final Logger logger = TrackLoggerFactory.getLogger(LogTrackAspect.class);

    @Pointcut("@annotation(com.wlqq.etc.common.log.annotations.LogTrack)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        boolean isSuccess = setMdc();
        try {
            // 执行方法，并获取返回值
            return joinPoint.proceed();
        } catch (Throwable t) {
            throw t;
        } finally {
            try {
                if (isSuccess) {
                    MDC.remove(MdcConstant.SESSION_KEY);
                    MDC.remove(MdcConstant.REQUEST_KEY);
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
        }
    }

    /**
     * 为每个请求设置唯一标示到MDC容器中
     *
     * @return
     */
    private boolean setMdc() {
        try {// 设置SessionId
            if (StringUtils.isEmpty(MDC.get(MdcConstant.REQUEST_KEY))) {
                String sessionId = UUID.randomUUID().toString();
                String requestId = UUID.randomUUID().toString().replace("-", "");
                MDC.put(MdcConstant.SESSION_KEY, sessionId);

                MDC.put(MdcConstant.REQUEST_KEY, requestId);
                return true;
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
        return false;
    }

}