package com.xiaolyuh.annotation;

import java.lang.annotation.*;

/**
 * 在输出日志前设置sessionID到MDC容器
 *
 * @author yuhao.wang3
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface LogTrack {

}