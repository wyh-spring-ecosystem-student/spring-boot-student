package com.xiaolyuh.aop.aspect;

import com.alibaba.fastjson.JSON;
import com.xiaolyuh.aop.annotations.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author yuhao.wang3
 */
// 切面
@Aspect
public class LogAspect {

    // 切点
    @Pointcut("@annotation(com.xiaolyuh.aop.annotations.Log)")
    public void pointCutMethod() {
    }

    @Pointcut("@within(com.xiaolyuh.aop.annotations.Log)")
    public void pointCutType() {
    }

    // 建言
    @Before("pointCutMethod() || pointCutType()")
//    @Before("execution(* com.xiaolyuh.aop.controller.UserController.*(..))")
    public void before(JoinPoint joinPoint) {
        // 通过反射可以获得注解上的属性，然后做日志记录相关的操作
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);
        System.out.println("日志切面 befor：" + log.value() + ":::" + JSON.toJSONString(joinPoint.getArgs()));
    }

    @After(value = "pointCutMethod() || pointCutType()")
    public void after(JoinPoint joinPoint) {
        // 通过反射可以获得注解上的属性，然后做日志记录相关的操作  
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);
        System.out.println("日志切面 after：" + log.value());
    }

    @AfterReturning(value = "pointCutMethod() || pointCutType()", returning = "result")
    public void afterReturning(JoinPoint joinPoint, Object result) {
        // 通过反射可以获得注解上的属性，然后做日志记录相关的操作
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);
        System.out.println("日志切面 afterReturning：" + log.value() + ":::" + JSON.toJSONString(result));
    }

    @AfterThrowing(value = "pointCutMethod() || pointCutType()", throwing = "t")
    public void afterThrowing(JoinPoint joinPoint, Throwable t) {
        // 通过反射可以获得注解上的属性，然后做日志记录相关的操作
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);
        System.out.println("日志切面 afterThrowing：" + log.value() + ":::" + JSON.toJSONString(t.getStackTrace()));
    }

//    @Around(value = "pointCutMethod() || pointCutType()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 通过反射可以获得注解上的属性，然后做日志记录相关的操作
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Log log = method.getAnnotation(Log.class);

        System.out.println("日志切面 Around before：" + log.value() + ":::" + JSON.toJSONString(joinPoint.getArgs()));
        Object result = joinPoint.proceed();
        System.out.println("日志切面 Around after：" + log.value() + ":::" + JSON.toJSONString(result));

        return result;
    }

}
