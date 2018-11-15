package com.xiaolyuh.aspect;


import com.alibaba.fastjson.JSON;
import com.xiaolyuh.annotation.Log;
import com.xiaolyuh.annotation.LogMask;
import com.xiaolyuh.annotation.LogTypeEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yuhao.wang
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Pointcut("@annotation(com.xiaolyuh.annotation.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Method method = this.getSpecificmethod(joinPoint);
        // 获取sessionId
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // 获取注解
        Log log = this.getMethodAnnotations(method, Log.class);
        if (Objects.isNull(log)) {
            log = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), Log.class);
        }
        // 获取日志输出前缀
        String prefix = getPrefix(log, method, requestAttributes.getSessionId());

        // 执行方法前输出日志
        logBefore(log, prefix, method, joinPoint.getArgs());
        // 执行方法，并获取返回值
        Object result = joinPoint.proceed();

        // 执行方法后输出日志
        logAfter(log, prefix, result);
        return result;
    }

    /**
     * 输出方法调用参数
     *
     * @param log    log注解
     * @param prefix 输出日志前缀
     * @param method 代理方法
     * @param args   方法参数
     */
    private void logBefore(Log log, String prefix, Method method, Object[] args) {
        // 判断是否是方法之后输出日志，不是就输出参数日志
        if (!LogTypeEnum.RESULT.equals(log.value())) {
            Map<String, Object> paramMap = new LinkedHashMap<>();
            // 获取参数注解
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();

            for (int i = 0; i < parameterAnnotations.length; i++) {
                Annotation[] parameterAnnotation = parameterAnnotations[i];
                // 找到参数上面的注解，并根据注解获取脱敏的类型
                LogMask logMask = getLogMask(parameterAnnotation);
                String paramName = "args" + (i + 1);
                if (logMask != null) {
                    paramName = StringUtils.isEmpty(logMask.paramName()) ? paramName : logMask.paramName();
                }

                // 忽略这些类型参数的输出
                if (args[i] instanceof ServletResponse || args[i] instanceof ServletRequest
                        || args[i] instanceof HttpSession || args[i] instanceof Model) {

                    break;
                }

                paramMap.put(paramName, args[i]);
            }
            logger.info("【请求参数 {}】：{}", prefix, JSON.toJSON(paramMap));
        }
    }

    /**
     * 输出方法执行结果
     *
     * @param log    log注解
     * @param prefix 输出前缀
     * @param result 方法执行结果
     */
    private void logAfter(Log log, String prefix, Object result) {
        // 判断是否是方法之前输出日志，不是就输出参数日志
        if (!LogTypeEnum.PARAMETER.equals(log.value())) {
            logger.info("【返回参数 {}】：{}", prefix, JSON.toJSON(result));
        }
    }

    /**
     * 获取日志前缀对象
     *
     * @param log       日志注解对象
     * @param method    注解日志的方法对象
     * @param sessionId
     * @return
     */
    private String getPrefix(Log log, Method method, String sessionId) {
        // 日志格式：流水号 + 注解的日志前缀 + 方法的全类名
        StringBuilder sb = new StringBuilder();
        sb.append(log.prefix());
        sb.append(":");
        sb.append(method.getDeclaringClass().getName());
        sb.append(".");
        sb.append(method.getName());
        sb.append("() ");

        return sb.toString();
    }

    /**
     * 获取参数上的LogMask注解
     *
     * @param parameterAnnotation
     * @return
     */
    private LogMask getLogMask(Annotation[] parameterAnnotation) {
        for (Annotation annotation : parameterAnnotation) {
            // 检查参数是否需要脱敏
            if (annotation instanceof LogMask) {
                return (LogMask) annotation;
            }
        }
        return null;
    }

    private <T extends Annotation> T getMethodAnnotations(AnnotatedElement ae, Class<T> annotationType) {
        // look for raw annotation
        T ann = ae.getAnnotation(annotationType);
        return ann;
    }

    private Method getSpecificmethod(ProceedingJoinPoint pjp) {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        // The method may be on an interface, but we need attributes from the
        // target class. If the target class is null, the method will be
        // unchanged.
        Class<?> targetClass = AopProxyUtils.ultimateTargetClass(pjp.getTarget());
        if (targetClass == null && pjp.getTarget() != null) {
            targetClass = pjp.getTarget().getClass();
        }
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
        // If we are dealing with method with generic parameters, find the
        // original method.
        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        return specificMethod;
    }

}