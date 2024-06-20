package com.alibaba.mos.api;

import lombok.experimental.UtilityClass;

@UtilityClass
public class EnvironmentConstant {

    /**
     * 各环境前缀
     */
    public static final String LOCAL = "local";
    public static final String TESTING = "testing";
    public static final String STAGING = "staging";
    public static final String PRODUCTION = "production";

    /**
     * 判断是否是否是日常环境
     *
     * @param env 环境标记
     */
    public static boolean isLocal(String env) {
        return LOCAL.equalsIgnoreCase(env);
    }

    /**
     * 判断是否是否是测试环境
     *
     * @param env 环境标记
     */
    public static boolean isTesting(String env) {
        return TESTING.equalsIgnoreCase(env);
    }

    /**
     * 判断是否是否是预发环境
     *
     * @param env 环境标记
     */
    public static boolean isStaging(String env) {
        return STAGING.equalsIgnoreCase(env);
    }

    /**
     * 判断是否是否是生产环境
     *
     * @param env 环境标记
     */
    public static boolean isProduction(String env) {
        return PRODUCTION.equalsIgnoreCase(env);
    }

    /**
     * 判断是否是真实环境
     * <p>
     * 真实环境是指日常/预发/生产
     *
     * @param env 环境标记
     */
    public static boolean isReal(String env) {
        return !isLocal(env);
    }

}
