package com.alibaba.mos.pantheon.rpc.api;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcProvider {
    /**
     * 服务名
     */
    String value() default "";
}
