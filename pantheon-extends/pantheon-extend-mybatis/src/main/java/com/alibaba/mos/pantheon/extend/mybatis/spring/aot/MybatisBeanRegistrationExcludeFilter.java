package com.alibaba.mos.pantheon.extend.mybatis.spring.aot;

import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.aot.BeanRegistrationExcludeFilter;
import org.springframework.beans.factory.support.RegisteredBean;

public class MybatisBeanRegistrationExcludeFilter implements BeanRegistrationExcludeFilter {

    @Override
    public boolean isExcludedFromAotProcessing(RegisteredBean registeredBean) {
        return MapperScannerConfigurer.class == registeredBean.getBeanClass();
    }

}
