package com.xiaolyuh.annotation;

import java.lang.annotation.*;

/**
 * 设置在输出日志钱需要设置sessionID到MDC容器
 *
 * @author yuhao.wang3
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogTrack {

}