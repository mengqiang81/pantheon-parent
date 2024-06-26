package com.alibaba.mos.pantheon.rpc.api;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcMethod {
    /**
     * 方法名
     */
    String value();
}
