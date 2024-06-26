package com.alibaba.mos.pantheon.extend.rpc.spring;

import lombok.Data;

import java.lang.reflect.Method;
import java.util.Map;

@Data
public class RpcProviderDefinition {

    /**
     * bean 名称
     */
    private String serviceName;

    /**
     * bean 对象
     */
    private Object serviceInstance;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 方法实例
     */
    private Method methodInstance;

    /**
     * 参数类型
     */
    private Map<String, Class<?>> paramTypes;


    /**
     * 返回类型
     */
    private Class<?> returnType;

}
