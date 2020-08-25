package com.xiaolyuh.aspect;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Maps;
import com.xiaolyuh.annotation.Log;
import com.xiaolyuh.annotation.LogIgnore;
import com.xiaolyuh.annotation.LogTypeEnum;
import com.xiaolyuh.core.TrackLoggerFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 日志输出切面
 *
 * @author yuhao.wang
 */
@Aspect
public class LogAspect {
    private static final Logger logger = TrackLoggerFactory.getLogger(LogAspect.class);

    private Map<String, String> prefixMap = Maps.newConcurrentMap();

    @Pointcut("@within(com.xiaolyuh.annotation.Log)")
    public void pointcutWithin() {
    }

    @Pointcut("@annotation(com.xiaolyuh.annotation.Log)")
    public void pointcutAnnotation() {
    }

    @Around("pointcutWithin() || pointcutAnnotation()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        Stopwatch stopwatch = Stopwatch.createStarted();
        Method method = getSpecificmethod(joinPoint);
        // 获取注解
        Log log = AnnotationUtils.findAnnotation(method, Log.class);
        if (Objects.isNull(log)) {
            log = AnnotationUtils.findAnnotation(joinPoint.getTarget().getClass(), Log.class);
        }
        // 获取日志输出前缀
        String prefix = getPrefix(log, method);

        // 执行方法前输出日志
        logBefore(log, prefix, method, joinPoint.getArgs());
        Object result = null;
        try {
            // 执行方法，并获取返回值
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            if (throwable.getClass().getSimpleName().toUpperCase().startsWith("ETC")) {
                Field errorCode = ReflectionUtils.findField(throwable.getClass(), "errorCode");
                Field errorMsg = ReflectionUtils.findField(throwable.getClass(), "errorMsg");
                if (Objects.nonNull(errorCode) && Objects.nonNull(errorMsg)) {
                    ReflectionUtils.makeAccessible(errorCode);
                    ReflectionUtils.makeAccessible(errorMsg);
                    logger.warn("异常【{}】 {}", prefix, ReflectionUtils.getField(errorMsg, throwable).toString(), throwable);

                    result = getResult(method, ReflectionUtils.getField(errorCode, throwable).toString(), ReflectionUtils.getField(errorMsg, throwable).toString());
                }
            }

            if (Objects.isNull(result)) {
                logger.error("异常【{}】 {}", prefix, throwable.getMessage(), throwable);
                result = getResult(method, "22159999", "系统异常");
            }
        } finally {
            // 执行方法后输出日志
            logAfter(log, prefix, result, stopwatch);
        }

        return result;
    }

    private Object getResult(Method method, String errorCode, String errorMsg) throws IllegalAccessException, InstantiationException {
        Object result = method.getReturnType().newInstance();
        // TODO
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
                if (isLogIgnore(parameterAnnotations[i])) {
                    continue;
                }
                String paramName = "args" + (i + 1);
                // 忽略这些类型参数的输出
                if (args[i] instanceof ServletResponse || args[i] instanceof ServletRequest
                        || args[i] instanceof HttpSession || args[i] instanceof Model
                        || args[i] instanceof MultipartFile) {

                    continue;
                }

                paramMap.put(paramName, args[i]);
            }
            logger.info("【请求参数 {}】：{}", prefix, JSON.toJSONString(paramMap));
        }
    }

    /**
     * 输出方法执行结果
     *
     * @param log    log注解
     * @param prefix 输出前缀
     * @param result 方法执行结果
     */
    private void logAfter(Log log, String prefix, Object result, Stopwatch stopwatch) {
        // 判断是否是方法之前输出日志，不是就输出参数日志
        if (!LogTypeEnum.PARAMETER.equals(log.value())) {
            logger.info("【返回参数 {} 】：{} ，耗时:{} 毫秒", prefix, JSON.toJSONString(result), stopwatch.elapsed(TimeUnit.MILLISECONDS));
        }
    }

    /**
     * 获取日志前缀对象
     *
     * @param log    日志注解对象
     * @param method 注解日志的方法对象
     * @return
     */
    private String getPrefix(Log log, Method method) {
        return prefixMap.computeIfAbsent(method.getDeclaringClass().getName() + "." + method.getName(), s -> {
            // 日志格式：流水号 + 注解的日志前缀 + 请求地址 + 方法的全类名
            StringBuilder sb = new StringBuilder();
            sb.append(log.prefix());
            sb.append(" ");

            sb.append("/");
            if (Objects.nonNull(RequestContextHolder.getRequestAttributes())) {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                sb.append(request.getRequestURI());
            }

            sb.append(" ");
            sb.append(":");
            sb.append(method.getDeclaringClass().getName());
            sb.append(".");
            sb.append(method.getName());
            sb.append("() ");

            return sb.toString().replace("///", "/").replace("//", "/");
        });


    }

    public static Method getSpecificmethod(ProceedingJoinPoint pjp) {
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

    /**
     * 获取参数上的LogIgnore注解
     *
     * @param parameterAnnotation
     * @return
     */
    private boolean isLogIgnore(Annotation[] parameterAnnotation) {
        for (Annotation annotation : parameterAnnotation) {
            // 检查参数是否需要脱敏
            if (annotation instanceof LogIgnore) {
                return true;
            }
        }
        return false;
    }
}