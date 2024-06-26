package com.alibaba.mos.pantheon.rpc.api;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RpcParam {
    /**
     * 方法名
     */
    String value();
}
