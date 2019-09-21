package com.xiaolyuh.aop.annotations;

import java.lang.annotation.*;

/**
 * 定义接口输入输出日志的切入点
 *
 * @author yuhao.wang3
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Log {
    String value() default "";
}
