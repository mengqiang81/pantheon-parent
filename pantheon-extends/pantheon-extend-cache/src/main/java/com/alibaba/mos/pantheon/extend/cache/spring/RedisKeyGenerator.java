package com.alibaba.mos.pantheon.extend.cache.spring;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.KotlinDetector;

import java.lang.reflect.Method;
import java.util.Arrays;

public class RedisKeyGenerator implements KeyGenerator {

    @Override
    @SuppressWarnings("NullableProblems")
    public Object generate(Object target, Method method, Object... params) {
        Object[] realParams = KotlinDetector.isSuspendingFunction(method)
                ? Arrays.copyOf(params, params.length - 1) : params;
        if (realParams.length == 0) {
            return target.getClass().getName() + ":" + method.getName();
        } else {
            return target.getClass().getName() + ":" + method.getName() + ":" + Arrays.deepHashCode(realParams);
        }
    }

}
