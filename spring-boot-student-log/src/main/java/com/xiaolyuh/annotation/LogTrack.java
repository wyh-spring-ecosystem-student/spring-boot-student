package com.xiaolyuh.annotation;

import java.lang.annotation.*;

/**
 * 设置在输出日志钱需要设置sessionID到MDC容器
 *
 * @author yuhao.wang3
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogTrack {

}