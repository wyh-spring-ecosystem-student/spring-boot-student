package com.xiaolyuh.annotation;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@LogTrack
@Inherited
@Documented
public @interface Log {

    /**
     * 打印日志类型，{@link LogTypeEnum}
     *
     * @return
     */
    LogTypeEnum value() default LogTypeEnum.ALL;

    /**
     * 日志输出前缀（建议配置接口名称）
     *
     * @return
     */
    String prefix() default "";
}
